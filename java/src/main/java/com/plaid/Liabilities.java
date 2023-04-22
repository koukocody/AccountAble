/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plaid;

import com.plaid.client.model.LiabilitiesGetRequest;
import com.plaid.client.model.LiabilitiesGetResponse;
import com.plaid.client.model.LiabilitiesObject;
import retrofit2.Response;
import static com.plaid.quickstart.QuickstartApplication.plaidClient;
import java.io.IOException;

/**
 *
 * @author cody6
 */
public class Liabilities {
    
    public static LiabilitiesObject getLiabilities(String accessToken) throws IOException {
        // Retrieve Liabilities data for an Item
        LiabilitiesGetRequest request = new LiabilitiesGetRequest().accessToken(accessToken);
        Response<LiabilitiesGetResponse> response = plaidClient.liabilitiesGet(request).execute();
        LiabilitiesObject liabilities = response.body().getLiabilities();
        
        return liabilities;
    }
}
