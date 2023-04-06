/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plaid;

import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Response;
import static com.plaid.quickstart.QuickstartApplication.plaidClient;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 *
 * @author cody6
 */
public class Transactions {
    
    public static List<Transaction> getTransactions(String start, String end, String accessToken) throws ParseException, IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse(start);
        LocalDate startDateLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date endDate = simpleDateFormat.parse(end);
        LocalDate endDateLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // Pull transactions for a date range
        TransactionsGetRequest request = new TransactionsGetRequest()
          .accessToken(accessToken)
          .startDate(startDateLocal)
          .endDate(endDateLocal);
        Response<TransactionsGetResponse>
          response = plaidClient.transactionsGet(request).execute();

        List<Transaction> transactions = new ArrayList <Transaction>();
        transactions.addAll(response.body().getTransactions());      
        
        return transactions;
    }
    

    
}
