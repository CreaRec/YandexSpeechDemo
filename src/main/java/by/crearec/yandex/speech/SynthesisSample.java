package by.crearec.yandex.speech;

import by.crearec.yandex.speech.utils.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SynthesisSample {

	public static void main(String[] args) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpRequest = new HttpPost(Constants.YANDEX_SPEECH_KIT_SYNTHESIS_URL);
			httpRequest.addHeader(Constants.AUTH_HEADER, Constants.AUTH_KEY);

			List<NameValuePair> postParameters = new ArrayList<>();
			String text = "Hello Yandex SpeechKit";
			postParameters.add(new BasicNameValuePair("text", text));
			postParameters.add(new BasicNameValuePair("voice", "oksana"));
			postParameters.add(new BasicNameValuePair("emotion", "good"));
			postParameters.add(new BasicNameValuePair("speed", "1.0"));
			postParameters.add(new BasicNameValuePair("format", "oggopus"));
			postParameters.add(new BasicNameValuePair("lang", "ru-RU"));
			postParameters.add(new BasicNameValuePair("sampleRateHertz", "48000"));
			httpRequest.setEntity(new UrlEncodedFormEntity(postParameters));

			try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpRequest)) {
				HttpEntity entity = closeableHttpResponse.getEntity();
				if (entity != null) {
					File tempFile = File.createTempFile("yandex-", "-synthesis.ogg");
					entity.writeTo(new FileOutputStream(tempFile));
				}
			}
		}
	}
}
