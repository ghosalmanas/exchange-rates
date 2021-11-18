package com.wholesale.utils;

public class Util {
   private static String sampleJsonToValidateFunctionality = "{\"success\":true,\"timeseries\":true,\"start_date\":\"2020-05-01\",\"end_date\":\"2020-05-03\",\"base\":\"EUR\",\"rates\":{\"2020-05-01\":{\"USD\":1.322891,\"GBP\":0.848047,\"HKD\":8.802303},\"2020-05-02\":{\"USD\":1.315066,\"GBP\":0.844202,\"HKD\":8.799083},\"2020-05-03\":{\"USD\":1.314491,\"GBP\":0.848049,\"HKD\":8.896868}}}";

   public static String prepareJson(){
       return sampleJsonToValidateFunctionality;
   }

}
