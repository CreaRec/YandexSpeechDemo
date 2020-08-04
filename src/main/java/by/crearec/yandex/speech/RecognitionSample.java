package by.crearec.yandex.speech;

import by.crearec.yandex.speech.dto.AudioDTO;
import by.crearec.yandex.speech.dto.ConfigDTO;
import by.crearec.yandex.speech.dto.LongRecognitionRequestDTO;
import by.crearec.yandex.speech.dto.OperationDTO;
import by.crearec.yandex.speech.dto.ResponseDTO;
import by.crearec.yandex.speech.dto.ResultRecognitionResponseDTO;
import by.crearec.yandex.speech.dto.SpecificationDTO;
import by.crearec.yandex.speech.utils.Constants;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RecognitionSample {
	public static void main(String[] args) throws IOException, InterruptedException {
		AWSCredentials credentials;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " + "Please make sure that your credentials file is at the correct " +
					"location (~/.aws/credentials), and is in valid format.", e);
		}

		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
		                                   .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration("storage.yandexcloud.net", "ru-central1")).build();

		Bucket bucket = s3.listBuckets().stream().filter(item -> Constants.TEST_BUCKET_NAME.equals(item.getName())).findFirst().orElse(null);
		if (bucket == null) {
			s3.createBucket(Constants.TEST_BUCKET_NAME);
		}
		File audioFile = getAudioFile();
		if (audioFile != null) {
			String key = Constants.TOP_DIRECTORY_NAME + "/" + audioFile.getName();
			s3.putObject(new PutObjectRequest(Constants.TEST_BUCKET_NAME, key, audioFile));
			URL url = s3.getUrl(Constants.TEST_BUCKET_NAME, key);

			LongRecognitionRequestDTO request = new LongRecognitionRequestDTO();
			ConfigDTO config = new ConfigDTO();
			SpecificationDTO specification = new SpecificationDTO();
			specification.setAudioChannelCount(1);
			specification.setAudioEncoding("OGG_OPUS");
			specification.setLanguageCode("ru-RU");
			specification.setProfanityFilter(false);
			specification.setSampleRateHertz(48000);
			config.setSpecification(specification);
			request.setConfig(config);
			AudioDTO audio = new AudioDTO();
			audio.setUri(url.toString());
			request.setAudio(audio);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				HttpPost httpRequest = new HttpPost(Constants.YANDEX_SPEECH_KIT_RUN_RECOGNIZE_URL);
				httpRequest.addHeader(Constants.AUTH_HEADER, Constants.AUTH_KEY);
				httpRequest.setEntity(new StringEntity(objectMapper.writeValueAsString(request), ContentType.APPLICATION_JSON));

				String operationId = null;
				try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpRequest)) {
					HttpEntity entity = closeableHttpResponse.getEntity();
					if (entity != null) {
						OperationDTO response = objectMapper.readValue(entity.getContent(), OperationDTO.class);
						if (response != null && response.getError() == null) {
							operationId = response.getId();
							Thread.sleep(10000);
						}
					}
				}
				if (operationId != null) {
					HttpGet httpGet = new HttpGet(Constants.YANDEX_SPEECH_KIT_RESULT_RECOGNIZE_URL + operationId);
					httpGet.addHeader(Constants.AUTH_HEADER, Constants.AUTH_KEY);
					try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet)) {
						HttpEntity entity = closeableHttpResponse.getEntity();
						if (entity != null) {
							ResultRecognitionResponseDTO response = objectMapper.readValue(entity.getContent(), ResultRecognitionResponseDTO.class);
							if (response != null && response.getError() == null && response.getDone() && response.getResponse() != null) {
								ResponseDTO result = response.getResponse();
								StringBuilder stringBuilder = new StringBuilder();
								result.getChunks().forEach(item -> stringBuilder.append(item.getAlternatives().get(0).getText()).append(" "));
								System.out.println(stringBuilder.toString());
							}
						}
					}
				}
				s3.deleteObject(Constants.TEST_BUCKET_NAME, key);
			}
		}
	}

	private static File getAudioFile() {
		URL resource = RecognitionSample.class.getClassLoader().getResource("test.ogg");
		if (resource != null) {
			return new File(resource.getFile());
		}
		return null;
	}
}
