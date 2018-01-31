

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class JsonDataParser implements Serializable{


    private static ObjectMapper mapper = new ObjectMapper();

   // public  static   void  main(String[] args) throws IOException {
// InputStream jsonStr = JsonDataParser.class.getClassLoader().getResourceAsStream("ps.json");

        public void putJsonDataParser(String jsonStr){
        Map<String,Object> resultMap =  new HashMap<String, Object>();

        ObjectMapper mapperObj = new ObjectMapper();

        OpenTSDBDataingestApp openTSDBDataingestApp =  new OpenTSDBDataingestApp();
        System.out.println("Input Json: " + jsonStr);
        try {
            resultMap = mapperObj.readValue(jsonStr,
                    new TypeReference<HashMap<String, Object>>() {
                    });
            System.out.println("Output Map: " + resultMap);

            for (Map.Entry entry : resultMap.entrySet()) {
                if (entry.getValue() instanceof HashMap) {
                    HashMap<String, Object> f = (HashMap<String, Object>) entry.getValue();
             /*        System.out.println("Output Map:" + f.toString());
                   System.out.println("Node ===>" + f.get("NODE"));
                    System.out.println("TIMESTAMP===>: " + f.get("TIMESTAMP"));
                    System.out.println("CPUSYSTEM====>" + f.get("CPUSYSTEM"));
                    System.out.println("MEMORYUSED====>" + f.get("MEMORYUSED"));
                    System.out.println("SERVUSEDSIZEMB====>" + f.get("SERVUSEDSIZEMB"));
                    System.out.println("NETWORK====>" + f.get("NETWORK"));
                    System.out.println("CPUUSER =====>" + f.get("CPUUSER"));
*/

                    for (Map.Entry entry1 : f.entrySet()){
                        if (entry1.getValue() instanceof HashMap) {
                            HashMap<String, Object> networkdata = (HashMap<String, Object>) entry1.getValue();
                            //System.out.println("value of n => : " + networkdata);

                           // System.out.println(" value of em1 => "+networkdata.get("em1"));

                            //Inner em1 data reading
                            for (Map.Entry entry2 : networkdata.entrySet()){
                                if (entry2.getValue() instanceof HashMap){
                                    HashMap<String,Object> emdata= (HashMap<String, Object>) entry2.getValue();
                                  /*  System.out.println("Bytes In  --> "+emdata.get("BYTESIN"));
                                    System.out.println("Bytes out  --> "+emdata.get("BYTESOUT"));
                                    System.out.println("Pakets out  --> "+emdata.get("PKTSIN"));
                                    System.out.println("Pkts out --> "+emdata.get("PKTSOUT"));
                                    System.out.println("======== END of each interation====");
*/
                                    String bytesInMetricData = "{\"metric\":\"BYTESIN\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + emdata.get("BYTESIN") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                                   // System.out.println("Bytes in MetricData ===>" + bytesInMetricData);
                                    openTSDBDataingestApp.putOpenTSDBdata(bytesInMetricData);

                                    String bytesOutMetricData = "{\"metric\":\"BYTESOUT\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + emdata.get("BYTESOUT") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                                    //System.out.println("Bytes Out MetricData ===>" + bytesOutMetricData);
                                    openTSDBDataingestApp.putOpenTSDBdata(bytesOutMetricData);

                                    String pktsInMetricData = "{\"metric\":\"PKTSIN\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + emdata.get("PKTSIN") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                                    //System.out.println("CPkts In MetricData ===>" + pktsInMetricData);
                                    openTSDBDataingestApp.putOpenTSDBdata(pktsInMetricData);

                                    String pktsOutMetricData = "{\"metric\":\"PKTSOUT\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + emdata.get("PKTSOUT") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                                    //System.out.println("Pkts Out MetricData ===>" + pktsOutMetricData);
                                    openTSDBDataingestApp.putOpenTSDBdata(pktsOutMetricData);

                                }
                            }

                        }
                    }
                    String cpuUserMetricData = "{\"metric\":\"CPUUSER\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + f.get("CPUUSER") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                    System.out.println("CPU UserMetricData ===>" + cpuUserMetricData);
                    int cpuReturnCode = openTSDBDataingestApp.putOpenTSDBdata(cpuUserMetricData);
                    System.out.println(cpuReturnCode);

                    String memoryUsedMetricData ="{\"metric\":\"MEMORYUSED\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + f.get("MEMORYUSED") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                    System.out.println("Memory UsedMetricData ===>" + memoryUsedMetricData);
                    int memoryReturnedCode =  openTSDBDataingestApp.putOpenTSDBdata(memoryUsedMetricData);
                    System.out.println(memoryReturnedCode );

                    String cpuSystemMetricData=    "{\"metric\":\"CPUSYSTEM\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + f.get("CPUSYSTEM") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                    System.out.println("CPU System MetricData ===>" + cpuSystemMetricData);
                    int cpuSystemReturnedCode = openTSDBDataingestApp.putOpenTSDBdata(cpuSystemMetricData);
                    System.out.println(cpuSystemReturnedCode);

                    String serverUsedSizeMbMetricData=  "{\"metric\":\"SERVUSEDSIZEMB\",\"timestamp\":\"" + f.get("TIMESTAMP") + "\", \"value\":\"" + f.get("SERVUSEDSIZEMB") + "\", \"tags\": { \"host\":\"" + f.get("NODE") + "\"}}";
                    System.out.println("Server Used SizeMB MetricData ===>" + serverUsedSizeMbMetricData);
                    int serverUsedReturnedCode =openTSDBDataingestApp.putOpenTSDBdata(serverUsedSizeMbMetricData);
                    System.out.println(serverUsedReturnedCode);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
