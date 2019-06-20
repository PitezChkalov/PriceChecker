package info.androidhive.recyclerviewswipe.service;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.support.Base64;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import info.androidhive.recyclerviewswipe.entity.Jewelry;
import info.androidhive.recyclerviewswipe.entity.Login;
import info.androidhive.recyclerviewswipe.entity.Order;
import info.androidhive.recyclerviewswipe.entity.User;
import timber.log.Timber;

/**
 * Created by Занятия on 08.12.2017.
 */

public class UserService implements IUserService {

    private static HttpHeaders getHeaders(Context context){

        SharedPreferences mySharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String plainCredentials=mySharedPreferences.getString("login", "")+":"
                + mySharedPreferences.getString("password", "");
        Timber.i(plainCredentials);
        String kek = "qwe:qwe";

        String base64Credentials = new String(Base64.encodeBytes(plainCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
    public static RestTemplate getRestTemplate() {
        // TODO: Fix the RestTemplate to be a singleton instance.
        RestTemplate restTemplate = new  RestTemplate();

        // Set the request factory.
        // IMPORTANT: This section I had to add for POST request. Not needed for GET
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Add converters
        // Note I use the Jackson Converter, I removed the http form converter
        // because it is not needed when posting String, used for multipart forms.
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }

    public String AddUser(String username, String password, Boolean enabled){
        Timber.i("AddUser: "+ username+ " "+ password+ " "+ enabled);

        try {

            RestTemplate restTemplate = getRestTemplate();
            System.out.println("\nTesting create User API----------");

            Object user = new User(username, password, true);
            HttpEntity<Object> request = new HttpEntity<Object>(user);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://baltsilverapp.herokuapp.com/" + "/user/add", request, String.class);
            HttpStatus status = response.getStatusCode();
            if (status == HttpStatus.CREATED)
                return "OK";
        }

        catch (HttpClientErrorException e) {
            Timber.e("AddUser "+ e.getMessage());
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            return e.getStatusCode().toString();
        }
        return null;
    }

    public User getUser(String barCode, Context context){
        Timber.i("getUser: "+ barCode);

            System.out.println("\nTesting getUser API----------");
            RestTemplate restTemplate = getRestTemplate();

            HttpEntity<String> request = new HttpEntity<String>(getHeaders(context));
            ResponseEntity<User> response = restTemplate.exchange("https://baltsilverapp.herokuapp.com/" +
                    "/user/user/"+ barCode, HttpMethod.GET, request, User.class);
            User user = response.getBody();
            System.out.println(user);
            return user;
        }

    public Boolean login(Login login, Context context ){
        Timber.i("login: "+ login);

        try {

            RestTemplate restTemplate = getRestTemplate();
            System.out.println("\nTesting create User API----------");

            HttpEntity<Object> request = new HttpEntity<Object>(login);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://baltsilverapp.herokuapp.com/" + "/user/login", request, String.class);
            HttpStatus status = response.getStatusCode();
                return true;
        }

        catch (HttpClientErrorException e) {
            Timber.e("login "+ e.getMessage());
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            return false;
        }
    }
    public Jewelry getJewelry(String barCode, Context context){
        Timber.i("getJewelry: "+ barCode);

        RestTemplate restTemplate = getRestTemplate();

        HttpEntity<String> request = new HttpEntity<String>(getHeaders(context));
        ResponseEntity<Jewelry> response = restTemplate.exchange("https://baltsilverapp.herokuapp.com/" +
                "/jewelry/get/"+ barCode, HttpMethod.GET, request, Jewelry.class);
        Jewelry jewelry = response.getBody();
        System.out.println(jewelry);
        return jewelry;
    }

    public HttpStatus sendOrder(List<Jewelry> jewelries, String user, Context context){
        Timber.i("sendOrder: "+ jewelries+" "+ user+ " ");
        try {
            Order order = new Order(jewelries,user);
            RestTemplate restTemplate = getRestTemplate();
            System.out.println("\nTesting create User API----------");

            HttpEntity<Object> request = new HttpEntity<Object>(order, getHeaders(context));
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://baltsilverapp.herokuapp.com/" + "/jewelry/sendOrder", request, String.class);
            HttpStatus status = response.getStatusCode();
            return status;
        }

        catch (HttpClientErrorException e) {
            Timber.e("sendOrder "+ e.getMessage());
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            return e.getStatusCode();
        }
    }


}


