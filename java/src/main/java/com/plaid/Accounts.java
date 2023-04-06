/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plaid;

import static Client.Client.accountIdAccessToken;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountsBalanceGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import java.text.ParseException;
import java.util.List;
import retrofit2.Response;
import static com.plaid.quickstart.QuickstartApplication.plaidClient;
import java.io.IOException;





/**
 *
 * @author cody6
 */
public class Accounts {
    
    public static List<AccountBase> getAccounts(String accessToken) throws ParseException, IOException {
        
        AccountsBalanceGetRequest request = new AccountsBalanceGetRequest().accessToken(accessToken);
        Response<AccountsGetResponse> response = plaidClient.accountsBalanceGet(request).execute();
        List<AccountBase> accounts = response.body().getAccounts();
        // key, value
        for (AccountBase account : accounts) {
            accountIdAccessToken.put(account.getAccountId(), accessToken);
        }
     
        return accounts;       
    }
}
