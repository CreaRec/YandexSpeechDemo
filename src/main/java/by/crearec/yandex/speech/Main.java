package by.crearec.yandex.speech;

import by.crearec.yandex.speech.dto.AudioDTO;
import by.crearec.yandex.speech.dto.ConfigDTO;
import by.crearec.yandex.speech.dto.LongSpeechRecognitionRequestDTO;
import by.crearec.yandex.speech.dto.LongSpeechRecognitionResponseDTO;
import by.crearec.yandex.speech.dto.SpecificationDTO;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class Main {
	private static final String TEST_BUCKET_NAME = "test-bucket";
	private static final String TOP_DIRECTORY_NAME = "records";
	private static final String YANDEX_SPEECH_KIT_URL_1 = "https://transcribe.api.cloud.yandex.net/speech/stt/v2/longRunningRecognize";
	private static final String YANDEX_SPEECH_KIT_URL_2 = "https://operation.api.cloud.yandex.net/operations/";
	private static final String AUTH_HEADER = "Authorization";
	private static final String AUTH_KEY = "Api-Key ";

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

		Bucket bucket = s3.listBuckets().stream().filter(item -> TEST_BUCKET_NAME.equals(item.getName())).findFirst().orElse(null);
		if (bucket == null) {
			s3.createBucket(TEST_BUCKET_NAME);
		}
		File audioFile = getAudioFile();
		if (audioFile != null) {
			String key = TOP_DIRECTORY_NAME + "/" + audioFile.getName();
			s3.putObject(new PutObjectRequest(TEST_BUCKET_NAME, key, audioFile));
			URL url = s3.getUrl(TEST_BUCKET_NAME, key);

			LongSpeechRecognitionRequestDTO request = new LongSpeechRecognitionRequestDTO();
			ConfigDTO config = new ConfigDTO();
			SpecificationDTO specification = new SpecificationDTO();
			specification.setAudioChannelCount(2);
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
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				HttpPost httpRequest = new HttpPost(YANDEX_SPEECH_KIT_URL_1);
				httpRequest.addHeader(AUTH_HEADER, AUTH_KEY);
				httpRequest.setEntity(new StringEntity(objectMapper.writeValueAsString(request), ContentType.APPLICATION_JSON));

				String operationId = null;
				try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpRequest)) {
					HttpEntity entity = closeableHttpResponse.getEntity();
					if (entity != null) {
						LongSpeechRecognitionResponseDTO response = objectMapper.readValue(entity.getContent(), LongSpeechRecognitionResponseDTO.class);
						if (response != null && response.getError() == null) {
							operationId = response.getId();
							Thread.sleep(5000);
						}
					}
				}
				if (operationId != null) {
					HttpGet httpGet = new HttpGet(YANDEX_SPEECH_KIT_URL_2 + operationId);
					httpGet.addHeader(AUTH_HEADER, AUTH_KEY);
					try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet)) {
						HttpEntity entity = closeableHttpResponse.getEntity();
						if (entity != null) {
							LongSpeechRecognitionResponseDTO response = objectMapper.readValue(entity.getContent(), LongSpeechRecognitionResponseDTO.class);
							if (response != null && response.getError() == null) {
								operationId = response.getId();
								Thread.sleep(5000);
							}
						}
					}
				}
				// TODO: request text by operation ID
			}
		}
	}

	private static File getAudioFile() {
		URL resource = Main.class.getClassLoader().getResource("test.ogg");
		if (resource != null) {
			return new File(resource.getFile());
		}
		return null;
	}
}
