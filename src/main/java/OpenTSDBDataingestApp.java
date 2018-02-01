import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class OpenTSDBDataingestApp {

//    ConfigParams configParams = new ConfigParams();

    /*
        public static  void  main(String[] args){
            OpenTSDBDataingestApp otsdbput = new OpenTSDBDataingestApp();
           // int i= otsdbput.putOpenTSDBdata(record);
        }*/
    public   int   putOpenTSDBdata(String str){
        System.out.println("String Value: "+str);
        System.out.println("####################### in OpenTSDB class data ingestion Method start ############################");

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(ConfigParams.openTSDBURL);


            //  StringEntity  urlParameters = new StringEntity ("{\"metric\":\"nayeem.test\",\"timestamp\":\"" + System.currentTimeMillis() + "\", \"value\": \"42\", \"tags\": { \"host\": \"chrisedwards\", \"dc\": \"lga\" }}");
            StringEntity urlParameters = new StringEntity(str);


            System.out.println("message is "+urlParameters.toString());

            request.setEntity(urlParameters);
            request.setHeader("Content-Type", "application/json; charset=UTF-8");
            System.out.println("####################### in OpenTSDB class data ingestion Method start  into DB ############################");
            HttpResponse response = httpClient.execute(request);
            System.out.println("####################### in OpenTSDB class data ingestion Method ENDS  into DB ############################");
            System.out.println(response);
            // handle response here...
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 1;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ab){
                ab.printStackTrace();
            }
        }
    }
}
