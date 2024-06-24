package com.myScm.scm.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudnaryConfig {

@SuppressWarnings("unchecked")
@Bean
public Cloudinary getCloudinary(){
    @SuppressWarnings("rawtypes")
    Map cloudprops = new HashMap();
    
    cloudprops.put("cloud_name", "dtitgyg0w");
    cloudprops.put("api_key", "374232221671846");
    cloudprops.put("api_secret", "Rlm5f2KWW6PP42vq6XJ4_PMG2-w");
    cloudprops.put("secure", true);
    

    
  return   new Cloudinary(cloudprops);
}


}
