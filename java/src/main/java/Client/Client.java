package Client;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author cody6
 */

import Login.LoginForm;
import static com.plaid.InitializeFirestore.db;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.plaid.Accounts;
import com.plaid.Liabilities;
import com.plaid.Transactions;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountSubtype;
import com.plaid.client.model.CreditCardLiability;
import com.plaid.client.model.LiabilitiesObject;
import com.plaid.client.model.MortgageLiability;
import com.plaid.client.model.StudentLoan;
import com.plaid.client.model.Transaction;
import com.plaid.quickstart.QuickstartApplication;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.CategoryLabelEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeries;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class Client extends javax.swing.JFrame {

    public static Map<String, String> accountIdAccessToken = new HashMap <>();
    final static Color NORMALTILECOLOR = new java.awt.Color(242, 243, 245);
    final static Color HOVERCOLOR = new java.awt.Color(255, 255, 255);
    final static Color ONCLICKTILECOLOR = new java.awt.Color(75, 75, 75);
    final int spendingNumberOfWeeks = 26;
    LocalDate todayMinusSpendingNumberOfWeeks = LocalDate.now().minusWeeks(spendingNumberOfWeeks);
    DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
    LocalDate startDateBasic = todayMinusSpendingNumberOfWeeks.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).minusDays(1);
    LocalDate endDateBasic = LocalDate.now().plusDays(1);
    boolean isBasicCategoryChart = true;
    DecimalFormat df = new DecimalFormat("#,###.00");
    double allAccountBalance = 0.00;
    double allAccountsLoans = 0.00;
    double allAccountsSavings = 0.00;
    double allocatedSavings = 0.00;
    double allAccountsInvestments = 0.00;
    int accountIteratorXAxis = 0;
    int accountIteratorYAxis = 0;
    int savingsIteratorXAxis = 0;
    int savingsIteratorYAxis = 0;
    int debtsIteratorXAxis = 0;
    int debtsIteratorYAxis = 0;
    int investmentIteratorXAxis = 0;
    int investmentIteratorYAxis = 0;
    int stockIteratorXAxis = 0;
    int stockIteratorYAxis = 0;
    int numberOfSavingsGoals = 0;
    Map<LocalDate, List<Transaction>> datedTransactions = new TreeMap<LocalDate, List<Transaction>>(Collections.reverseOrder());
    List<String> savingsAccountList = new ArrayList <>();
    Map<String, List<Transaction>> savingsAccountTransactionList = new TreeMap<>(Collections.reverseOrder());
    List<String> depositoryList = Arrays.asList("depository", "checking", "paypal", "prepaid", "cash management", "ebt");
    List<String> savingsList = Arrays.asList("savings", "hsa", "cd", "money market");
    List<String> creditList = Arrays.asList("credit", "credit card", "paypal");
    List<String> loanList = Arrays.asList("loan", "auto", "business", "commercial", "construction", "consumer", "home equity", "mortgage", "overdraft", "line of credit", "student");
    List<String> investmentList = Arrays.asList("investment", "529", "401a", "401k", "403B", "457b", "brokerage", "cash isa", "crypto exchange", "education savings account", "fixed annuity", "gic", "health reimbursement arrangement", "hsa", "ira", "isa", "keogh", "lif", "life insurance", "lira", "lrif", "lrsp", "mutual fund", "non-custodial wallet", "non-taxable brokerage account", "other annuity", "other insurance", "pension", "prif", "profit sharing plan", "qshr", "rdsp", "resp", "retirement", "rlif", "roth", "roth 401k", "rrif", "rrsp", "sarsep", "sep ira", "simple ira", "sipp", "stock plan", "tfsa", "trust", "ugma", "utma", "variable annuity");
    List<Transaction> transactionsList = new ArrayList <>();
    List<AccountBase> accountsList = new ArrayList <>();
    List<LiabilitiesObject> liabilitiesList = new ArrayList <>();
    
    
    
    /**
     * Creates new form Client
     * @throws java.lang.InterruptedException
     * @throws java.text.ParseException
     * @throws java.util.concurrent.ExecutionException
     */
    public Client() throws InterruptedException, ParseException, ExecutionException, IOException {
        
        long start, end; 
        
        System.out.println("start initComponents();");
        start = System.currentTimeMillis();
        initComponents();
        end = System.currentTimeMillis();
        System.out.println("initComponents end " + (end - start) + " ms");
        
        System.out.println("start Transacations and Accounts pull!!");
        start = System.currentTimeMillis();
        try {
            ApiFuture<QuerySnapshot> futureTokens = db.collection("users").document(QuickstartApplication.userID).collection("tokens").get();
            List<QueryDocumentSnapshot> tokens = futureTokens.get().getDocuments();
            if (tokens.isEmpty()) {
                masterTabPane.setSelectedIndex(1);
                masterTabPane.setEnabledAt(0, false);
                masterTabPane.setEnabledAt(2, false);
                masterTabPane.setEnabledAt(3, false);
                masterTabPane.setEnabledAt(4, false);
                masterTabPane.setEnabledAt(5, false);
                newAccountDialog.setVisible(true);
            }
            for (DocumentSnapshot  token : tokens) {
                transactionsList.addAll(Transactions.getTransactions(java.time.LocalDate.now().minusYears(1).toString(), java.time.LocalDate.now().toString(), token.getId()));
                accountsList.addAll(Accounts.getAccounts(token.getId()));
                liabilitiesList.add(Liabilities.getLiabilities(token.getId()));
            }      
            int i = transactionsList.size() - 1;
            for (Transaction transaction : transactionsList) {
                List<Transaction> tempList = new ArrayList<>();
                LocalDate transactionDate = transaction.getDate();
                if (datedTransactions.containsKey(transactionDate)) {
                    datedTransactions.get(transactionDate).add(transaction);
                } else {
                    tempList.add(transaction);
                    datedTransactions.put(transactionDate, tempList);
                }
                i--;
            }
        } catch (ExecutionException | ParseException | IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        end = System.currentTimeMillis();
        System.out.println("End Transacations and Accounts pull!! " + (end - start) + " ms");
        
        System.out.println("start accountPanelPopulation();");
        start = System.currentTimeMillis();
        accountPanelPopulation();
        end = System.currentTimeMillis();
        System.out.println("accountPanelPopulation end " + (end - start) + " ms");
        
        System.out.println("start recentTransactionsPopulation();");
        start = System.currentTimeMillis();
        recentTransactionsPopulation();        
        end = System.currentTimeMillis();
        System.out.println("recentTransactionsPopulation end " + (end - start) + " ms");
        
        System.out.println("start createHomeTabChart();");
        start = System.currentTimeMillis();
        createHomeTabChart();
        end = System.currentTimeMillis();
        System.out.println("createHomeTabChart end " + (end - start) + " ms");
        
        try {
            System.out.println("start savingsGoals();");
            start = System.currentTimeMillis();
            savingsGoals();
            end = System.currentTimeMillis();
            System.out.println("savingsGoals end " + (end - start) + " ms");
        } catch (ExecutionException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
                   
        System.out.println("start savingsTransactionsPopulation();");
        start = System.currentTimeMillis();
        savingsTableAndGraphHistory();
        end = System.currentTimeMillis();
        System.out.println("savingsTransactionsPopulation end " + (end - start) + " ms");
        
        System.out.println("start spendingTableAndGraphHistory();");
        start = System.currentTimeMillis();
        spendingTableAndGraphHistory();
        end = System.currentTimeMillis();
        System.out.println("spendingTableAndGraphHistory end " + (end - start) + " ms");
        
        System.out.println("start spendingCategoryChart();");
        start = System.currentTimeMillis();
        spendingCategoryChart(startDateBasic, endDateBasic, "Past " + spendingNumberOfWeeks + " Weeks");
        end = System.currentTimeMillis();
        System.out.println("spendingCategoryChart end " + (end - start) + " ms");        
        
        System.out.println("start stockPanelPopulation();");
        start = System.currentTimeMillis();
        stockPanelPopulation();
        end = System.currentTimeMillis();
        System.out.println("stockPanelPopulation end " + (end - start) + " ms");   
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addGoalDialog = new javax.swing.JDialog();
        addGoalPanel = new javax.swing.JPanel();
        addGoalContainer = new javax.swing.JPanel();
        addGoalItemDescLabel = new javax.swing.JLabel();
        addGoalItemDescField = new javax.swing.JFormattedTextField();
        addGoalAmountSavedLabel = new javax.swing.JLabel();
        DecimalFormat dfJFTF = new DecimalFormat("#.00");
        addGoalAmountSavedField = new javax.swing.JFormattedTextField(dfJFTF);
        addGoalGoalAmountLabel = new javax.swing.JLabel();
        addGoalGoalAmountField = new javax.swing.JFormattedTextField(dfJFTF);
        addGoalBeginDateLabel = new javax.swing.JLabel();
        addGoalDateFormatLabel1 = new javax.swing.JLabel();
        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        addGoalBeginDateField = new javax.swing.JFormattedTextField(dateFormat);
        addGoalEndDateLabel = new javax.swing.JLabel();
        addGoalDateFormatLabel2 = new javax.swing.JLabel();
        addGoalEndDateField = new javax.swing.JFormattedTextField(dateFormat);
        addGoalButton = new javax.swing.JButton();
        newAccountDialog = new javax.swing.JDialog();
        newAccountPanel = new javax.swing.JPanel();
        newAccountContinueButton = new javax.swing.JButton();
        newAccountScrollPane = new javax.swing.JScrollPane();
        newAccountTextField = new javax.swing.JTextArea();
        addStockDialog = new javax.swing.JDialog();
        addStockPanel = new javax.swing.JPanel();
        stockDialogTickerLabel = new javax.swing.JLabel();
        stockDialogTickerTextField = new javax.swing.JTextField();
        stockDialogSharesLabel = new javax.swing.JLabel();
        stockDialogSharesTextField = new javax.swing.JTextField();
        stockDialogCostLabel = new javax.swing.JLabel();
        stockDialogCostTextField = new javax.swing.JTextField();
        stockDialogDateLabel = new javax.swing.JLabel();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        stockDialogDateTextField = new javax.swing.JFormattedTextField(dateFormat1);
        stockDialogDateFormatLabel = new javax.swing.JLabel();
        stockDialogAddOrEditButton = new javax.swing.JButton();
        stockDialogDeleteButton = new javax.swing.JButton();
        masterTabPane = new javax.swing.JTabbedPane();
        homeTab = new javax.swing.JPanel();
        transactionsScrollPane = new javax.swing.JScrollPane();
        transactionsPanel = new javax.swing.JPanel();
        //For some reason deleting this button breaks the app
        jButton1 = new javax.swing.JButton();
        recentTransactionsLabel = new javax.swing.JLabel();
        listedTransactionsPanel = new javax.swing.JPanel();
        incomeAndExpenseGraph = new javax.swing.JPanel();
        homeTilesPanel = new javax.swing.JPanel();
        balanceTile = new javax.swing.JPanel();
        balanceLabel = new javax.swing.JLabel();
        totalBalance = new javax.swing.JLabel();
        savingsTile = new javax.swing.JPanel();
        savingsLabel = new javax.swing.JLabel();
        totalSavings = new javax.swing.JLabel();
        loansTile = new javax.swing.JPanel();
        loansLabel = new javax.swing.JLabel();
        totalLoans = new javax.swing.JLabel();
        investmentsTile = new javax.swing.JPanel();
        investmentsLabel = new javax.swing.JLabel();
        totalInvestments = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        accountTab = new javax.swing.JPanel();
        linkButton = new javax.swing.JButton();
        accountScrollPanel = new javax.swing.JScrollPane();
        accountPanel = new javax.swing.JPanel();
        profileReloadButton = new javax.swing.JButton();
        savingsTab = new javax.swing.JPanel();
        savingsScrollPane = new javax.swing.JScrollPane();
        savingsPanel = new javax.swing.JPanel();
        goalsInfoPanel = new javax.swing.JPanel();
        goalsAllocationPanel = new javax.swing.JPanel();
        availableAllocationLabel = new javax.swing.JLabel();
        actualAllocationLabel = new javax.swing.JLabel();
        availableAmountAllocationLabel = new javax.swing.JLabel();
        actualAmountAllocationLabel = new javax.swing.JLabel();
        saveGoalEditsButton = new javax.swing.JButton();
        goalsTitlePanel = new javax.swing.JPanel();
        initiateGoalDialogButton = new javax.swing.JButton();
        goalsLabelPanel = new javax.swing.JLabel();
        goalsScrollPane = new javax.swing.JScrollPane();
        goalsPanel = new javax.swing.JPanel();
        goalsTableScrollPane = new javax.swing.JScrollPane();
        goalsTable = new javax.swing.JTable();
        savingsQuickHistoryLabel = new javax.swing.JLabel();
        savingsQuickHistoryScrollPane = new javax.swing.JScrollPane();
        savingsQuickHistoryTable = new javax.swing.JTable();
        savingsGraph = new javax.swing.JPanel();
        debtsTab = new javax.swing.JPanel();
        debtsScrollPanel = new javax.swing.JScrollPane();
        debtsPanel = new javax.swing.JPanel();
        investmentsTab = new javax.swing.JPanel();
        investmentsAccountScrollPanel = new javax.swing.JScrollPane();
        investmentsAccountPanel = new javax.swing.JPanel();
        investmentsStockPanel = new javax.swing.JPanel();
        investmentsPanelStockLabel = new javax.swing.JLabel();
        investmentsPanelAddStock = new javax.swing.JButton();
        investmentsDisplayStocksScrollPane = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        spendingTab = new javax.swing.JPanel();
        spendingGraph = new javax.swing.JPanel();
        spendingScrollPane = new javax.swing.JScrollPane();
        spendingTable = new javax.swing.JTable();
        spendingLabel = new javax.swing.JLabel();
        spendingPieChart = new javax.swing.JPanel();
        accountPopupTab = new javax.swing.JPanel();
        accountDetailsPopup = new javax.swing.JPanel();
        popupAccountName = new javax.swing.JLabel();
        popupSeperatorLabel = new javax.swing.JLabel();
        popupAccountType = new javax.swing.JLabel();
        popupAccountMaskedNumber = new javax.swing.JLabel();
        popupBalance = new javax.swing.JLabel();
        transactionsLabel = new javax.swing.JLabel();
        payBillButton = new javax.swing.JButton();
        availableCreditPanel = new javax.swing.JPanel();
        availableCreditLabel = new javax.swing.JLabel();
        popupAvailableCredit = new javax.swing.JLabel();
        minimumPaymentPanel = new javax.swing.JPanel();
        minPaymentLabel = new javax.swing.JLabel();
        popupMinPayment = new javax.swing.JLabel();
        dueDatePanel = new javax.swing.JPanel();
        dueDateLabel = new javax.swing.JLabel();
        popupDueDate = new javax.swing.JLabel();
        cards = new javax.swing.JPanel();
        studentCard = new javax.swing.JPanel();
        studentLayoutPanel = new javax.swing.JPanel();
        studentLabel1 = new javax.swing.JLabel();
        studentLabel2 = new javax.swing.JLabel();
        studentLabel3 = new javax.swing.JLabel();
        studentLabel4 = new javax.swing.JLabel();
        studentLabel5 = new javax.swing.JLabel();
        studentLabel6 = new javax.swing.JLabel();
        studentLabel7 = new javax.swing.JLabel();
        studentLabel8 = new javax.swing.JLabel();
        studentLabel9 = new javax.swing.JLabel();
        studentLabel10 = new javax.swing.JLabel();
        studentLabel11 = new javax.swing.JLabel();
        studentLabel12 = new javax.swing.JLabel();
        tableCard = new javax.swing.JPanel();
        transactionsScrollPopup = new javax.swing.JScrollPane();
        transactionsTable = new javax.swing.JTable();
        mortgageCard = new javax.swing.JPanel();
        mortgageLayoutPanel = new javax.swing.JPanel();
        mortgageLabel1 = new javax.swing.JLabel();
        mortgageLabel2 = new javax.swing.JLabel();
        mortgageLabel3 = new javax.swing.JLabel();
        mortgageLabel4 = new javax.swing.JLabel();
        mortgageLabel5 = new javax.swing.JLabel();
        mortgageLabel6 = new javax.swing.JLabel();
        mortgageLabel7 = new javax.swing.JLabel();
        mortgageLabel8 = new javax.swing.JLabel();
        mortgageLabel9 = new javax.swing.JLabel();
        closeAppPanel = new javax.swing.JLayeredPane();
        closeLabel = new javax.swing.JLabel();
        accountableHeaderPanel = new javax.swing.JLayeredPane();
        accountableHeaderLabel = new javax.swing.JLabel();

        addGoalDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addGoalDialog.setTitle("Add to Savings Table");
        addGoalDialog.setIconImage(null);
        addGoalDialog.setLocation(new java.awt.Point(0, 0));
        addGoalDialog.setSize(new java.awt.Dimension(325, 450));
        addGoalDialog.setType(java.awt.Window.Type.POPUP);
        addGoalDialog.setLocationRelativeTo(null);

        addGoalItemDescLabel.setText("Goal item");

        addGoalItemDescField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                addGoalItemDescFieldKeyTyped(evt);
            }
        });

        addGoalAmountSavedLabel.setText("Amount saved");

        addGoalGoalAmountLabel.setText("Goal amount");

        addGoalBeginDateLabel.setText("Begin date     ");

        addGoalDateFormatLabel1.setText("MM/DD/YYYY");

        addGoalEndDateLabel.setText("End date     ");

        addGoalDateFormatLabel2.setText("MM/DD/YYYY");

        addGoalButton.setText("Add savings goal");
        addGoalButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addGoalButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout addGoalContainerLayout = new javax.swing.GroupLayout(addGoalContainer);
        addGoalContainer.setLayout(addGoalContainerLayout);
        addGoalContainerLayout.setHorizontalGroup(
            addGoalContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addGoalEndDateField)
            .addComponent(addGoalBeginDateField, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(addGoalGoalAmountField, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(addGoalAmountSavedField, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(addGoalItemDescField, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addGoalContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addGoalContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addGoalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addGoalContainerLayout.createSequentialGroup()
                        .addComponent(addGoalBeginDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                        .addComponent(addGoalDateFormatLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addGoalContainerLayout.createSequentialGroup()
                        .addGroup(addGoalContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addGoalItemDescLabel)
                            .addComponent(addGoalAmountSavedLabel)
                            .addComponent(addGoalGoalAmountLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addGoalContainerLayout.createSequentialGroup()
                        .addComponent(addGoalEndDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addGoalDateFormatLabel2)))
                .addContainerGap())
        );
        addGoalContainerLayout.setVerticalGroup(
            addGoalContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addGoalContainerLayout.createSequentialGroup()
                .addComponent(addGoalItemDescLabel)
                .addGap(7, 7, 7)
                .addComponent(addGoalItemDescField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addGoalAmountSavedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addGoalAmountSavedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addGoalGoalAmountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addGoalGoalAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addGoalContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addGoalBeginDateLabel)
                    .addComponent(addGoalDateFormatLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addGoalBeginDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addGoalContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addGoalEndDateLabel)
                    .addComponent(addGoalDateFormatLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addGoalEndDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(addGoalButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout addGoalPanelLayout = new javax.swing.GroupLayout(addGoalPanel);
        addGoalPanel.setLayout(addGoalPanelLayout);
        addGoalPanelLayout.setHorizontalGroup(
            addGoalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addGoalContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        addGoalPanelLayout.setVerticalGroup(
            addGoalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addGoalPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(addGoalContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout addGoalDialogLayout = new javax.swing.GroupLayout(addGoalDialog.getContentPane());
        addGoalDialog.getContentPane().setLayout(addGoalDialogLayout);
        addGoalDialogLayout.setHorizontalGroup(
            addGoalDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addGoalDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addGoalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        addGoalDialogLayout.setVerticalGroup(
            addGoalDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addGoalDialogLayout.createSequentialGroup()
                .addComponent(addGoalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        newAccountDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        newAccountDialog.setTitle("Welcome!");
        newAccountDialog.setAlwaysOnTop(true);
        newAccountDialog.setResizable(false);
        newAccountDialog.setType(java.awt.Window.Type.POPUP);

        newAccountPanel.setLayout(new java.awt.BorderLayout());

        newAccountContinueButton.setText("Continue");
        newAccountContinueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newAccountContinueButtonMouseClicked(evt);
            }
        });
        newAccountPanel.add(newAccountContinueButton, java.awt.BorderLayout.PAGE_END);

        newAccountScrollPane.setHorizontalScrollBar(null);

        newAccountTextField.setEditable(false);
        newAccountTextField.setColumns(20);
        newAccountTextField.setForeground(new java.awt.Color(0, 0, 0));
        newAccountTextField.setLineWrap(true);
        newAccountTextField.setRows(5);
        newAccountTextField.setText("Welcome to AccountAble!\n\n\nBefore continuing, please read the instructions in this window.\n\nTo begin, press the \"Launch Link\" button. This will enable \nyou to connect your AccountAble profile toyour financial institutions, \none at a time.\n\nTo add multiple financial institutions, you must complete this process\nas many times as needed. Be sure to completely follow the prompts\nand close any Link windows only when instructed to do so.\n\nFor the best experience, please wait until all necessary financial\ninstitutions are connected before pressing the \"Reload Profile\" button.\n\n\nThank you for being an AccountAble member!");
        newAccountTextField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        newAccountScrollPane.setViewportView(newAccountTextField);

        newAccountPanel.add(newAccountScrollPane, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout newAccountDialogLayout = new javax.swing.GroupLayout(newAccountDialog.getContentPane());
        newAccountDialog.getContentPane().setLayout(newAccountDialogLayout);
        newAccountDialogLayout.setHorizontalGroup(
            newAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newAccountPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        newAccountDialogLayout.setVerticalGroup(
            newAccountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newAccountPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        newAccountDialog.setLocation(new java.awt.Point(0, 0));
        newAccountDialog.setSize(450, 380);
        newAccountDialog.setLocationRelativeTo(null);

        addStockDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addStockDialog.setTitle("Add to Stock Tracking");
        addStockDialog.setAlwaysOnTop(true);
        addStockDialog.setIconImage(null);
        addStockDialog.setPreferredSize(new java.awt.Dimension(350, 450));
        addStockDialog.setSize(new java.awt.Dimension(350, 430));
        addStockDialog.setType(java.awt.Window.Type.POPUP);
        addStockDialog.setLocation(new java.awt.Point(0, 0));
        addStockDialog.setLocationRelativeTo(null);

        stockDialogTickerLabel.setText("Stock Symbol (e.g TSLA, MSFT)");

        stockDialogSharesLabel.setText("Number of Shares");

        stockDialogCostLabel.setText("Cost per Share");

        stockDialogDateLabel.setText("Initial Purchase Date");

        stockDialogDateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockDialogDateTextFieldActionPerformed(evt);
            }
        });

        stockDialogDateFormatLabel.setText("YYYY-MM-DD");

        stockDialogAddOrEditButton.setText("Add");
        stockDialogAddOrEditButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockDialogAddOrEditButtonMouseClicked(evt);
            }
        });

        stockDialogDeleteButton.setText("Delete");
        stockDialogDeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockDialogDeleteButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout addStockPanelLayout = new javax.swing.GroupLayout(addStockPanel);
        addStockPanel.setLayout(addStockPanelLayout);
        addStockPanelLayout.setHorizontalGroup(
            addStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addStockPanelLayout.createSequentialGroup()
                .addGroup(addStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addStockPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(addStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stockDialogTickerLabel)
                            .addComponent(stockDialogSharesLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(addStockPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(addStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stockDialogDeleteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(stockDialogTickerTextField)
                            .addComponent(stockDialogSharesTextField)
                            .addComponent(stockDialogCostTextField)
                            .addComponent(stockDialogDateTextField)
                            .addComponent(stockDialogAddOrEditButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(addStockPanelLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(addStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(addStockPanelLayout.createSequentialGroup()
                                        .addComponent(stockDialogDateLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                                        .addComponent(stockDialogDateFormatLabel))
                                    .addGroup(addStockPanelLayout.createSequentialGroup()
                                        .addComponent(stockDialogCostLabel)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addContainerGap())
        );
        addStockPanelLayout.setVerticalGroup(
            addStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addStockPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(stockDialogTickerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stockDialogTickerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stockDialogSharesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stockDialogSharesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stockDialogCostLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stockDialogCostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(addStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stockDialogDateLabel)
                    .addComponent(stockDialogDateFormatLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stockDialogDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(stockDialogAddOrEditButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stockDialogDeleteButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addStockDialogLayout = new javax.swing.GroupLayout(addStockDialog.getContentPane());
        addStockDialog.getContentPane().setLayout(addStockDialogLayout);
        addStockDialogLayout.setHorizontalGroup(
            addStockDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addStockPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        addStockDialogLayout.setVerticalGroup(
            addStockDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addStockPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        masterTabPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75), 3));
        masterTabPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        masterTabPane.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        masterTabPane.setMinimumSize(new java.awt.Dimension(529, 60));
        masterTabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                masterTabPaneStateChanged(evt);
            }
        });

        transactionsScrollPane.setBackground(new java.awt.Color(242, 243, 245));
        transactionsScrollPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(75, 75, 75), 2, true));
        transactionsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        transactionsScrollPane.setAutoscrolls(true);

        transactionsPanel.setBackground(new java.awt.Color(242, 243, 245));
        transactionsPanel.setAlignmentX(0.0F);
        transactionsPanel.setAlignmentY(0.0F);
        transactionsPanel.setAutoscrolls(true);

        jButton1.setVisible(false);
        jButton1.setText("do not remove");

        recentTransactionsLabel.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        recentTransactionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        recentTransactionsLabel.setText("do not remove");
        recentTransactionsLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        recentTransactionsLabel.setAlignmentY(0.0F);
        recentTransactionsLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        recentTransactionsLabel.setVisible(false);

        listedTransactionsPanel.setBackground(new java.awt.Color(242, 243, 245));
        listedTransactionsPanel.setLayout(new javax.swing.BoxLayout(listedTransactionsPanel, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout transactionsPanelLayout = new javax.swing.GroupLayout(transactionsPanel);
        transactionsPanel.setLayout(transactionsPanelLayout);
        transactionsPanelLayout.setHorizontalGroup(
            transactionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transactionsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(transactionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listedTransactionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(transactionsPanelLayout.createSequentialGroup()
                        .addComponent(recentTransactionsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addGap(20, 20, 20))
        );
        transactionsPanelLayout.setVerticalGroup(
            transactionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transactionsPanelLayout.createSequentialGroup()
                .addGroup(transactionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(transactionsPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(recentTransactionsLabel))
                    .addGroup(transactionsPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jButton1)))
                .addGap(0, 0, 0)
                .addComponent(listedTransactionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        transactionsScrollPane.setViewportView(transactionsPanel);

        incomeAndExpenseGraph.setBackground(new java.awt.Color(67, 67, 67));
        incomeAndExpenseGraph.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(75, 75, 75), 2, true));
        incomeAndExpenseGraph.setLayout(new javax.swing.BoxLayout(incomeAndExpenseGraph, javax.swing.BoxLayout.Y_AXIS));

        balanceTile.setBackground(new java.awt.Color(242, 243, 245));
        balanceTile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        balanceTile.setForeground(new java.awt.Color(170, 170, 170));
        balanceTile.setToolTipText("Balance equals depository account balances (i.e. checking) minus credit account balances (i.e. credit cards)");
        tileClickColorChange(balanceTile);
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 14));
        balanceTile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                balanceTileMouseClicked(evt);
            }
        });

        balanceLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        balanceLabel.setText("Balance");

        totalBalance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalBalance.setText("$0.00");

        javax.swing.GroupLayout balanceTileLayout = new javax.swing.GroupLayout(balanceTile);
        balanceTile.setLayout(balanceTileLayout);
        balanceTileLayout.setHorizontalGroup(
            balanceTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, balanceTileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(balanceTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(balanceLabel)
                    .addComponent(totalBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                .addGap(97, 97, 97))
        );
        balanceTileLayout.setVerticalGroup(
            balanceTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(balanceTileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(balanceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalBalance)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        savingsTile.setBackground(new java.awt.Color(242, 243, 245));
        savingsTile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tileClickColorChange(savingsTile);
        savingsTile.setForeground(new java.awt.Color(170, 170, 170));
        savingsTile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                savingsTileMouseClicked(evt);
            }
        });

        savingsLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        savingsLabel.setText("Savings");

        totalSavings.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalSavings.setText("$0.00");

        javax.swing.GroupLayout savingsTileLayout = new javax.swing.GroupLayout(savingsTile);
        savingsTile.setLayout(savingsTileLayout);
        savingsTileLayout.setHorizontalGroup(
            savingsTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, savingsTileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(savingsTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(savingsLabel)
                    .addComponent(totalSavings, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                .addGap(97, 97, 97))
        );
        savingsTileLayout.setVerticalGroup(
            savingsTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(savingsTileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(savingsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalSavings)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        loansTile.setBackground(new java.awt.Color(242, 243, 245));
        loansTile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        loansTile.setForeground(new java.awt.Color(170, 170, 170));
        tileClickColorChange(loansTile);
        loansTile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loansTileMouseClicked(evt);
            }
        });

        loansLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        loansLabel.setText("Loans");

        totalLoans.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalLoans.setText("$0.00");

        javax.swing.GroupLayout loansTileLayout = new javax.swing.GroupLayout(loansTile);
        loansTile.setLayout(loansTileLayout);
        loansTileLayout.setHorizontalGroup(
            loansTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loansTileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loansTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalLoans, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(loansLabel)))
        );
        loansTileLayout.setVerticalGroup(
            loansTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loansTileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loansLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalLoans)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        investmentsTile.setBackground(new java.awt.Color(242, 243, 245));
        investmentsTile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        investmentsTile.setForeground(new java.awt.Color(170, 170, 170));
        tileClickColorChange(investmentsTile);
        investmentsTile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                investmentsTileMouseClicked(evt);
            }
        });

        investmentsLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        investmentsLabel.setText("Investments");

        totalInvestments.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalInvestments.setText("$0.00");

        javax.swing.GroupLayout investmentsTileLayout = new javax.swing.GroupLayout(investmentsTile);
        investmentsTile.setLayout(investmentsTileLayout);
        investmentsTileLayout.setHorizontalGroup(
            investmentsTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, investmentsTileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(investmentsTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(investmentsLabel)
                    .addComponent(totalInvestments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54))
        );
        investmentsTileLayout.setVerticalGroup(
            investmentsTileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(investmentsTileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(investmentsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalInvestments)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout homeTilesPanelLayout = new javax.swing.GroupLayout(homeTilesPanel);
        homeTilesPanel.setLayout(homeTilesPanelLayout);
        homeTilesPanelLayout.setHorizontalGroup(
            homeTilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTilesPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(homeTilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(homeTilesPanelLayout.createSequentialGroup()
                        .addComponent(balanceTile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(savingsTile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, homeTilesPanelLayout.createSequentialGroup()
                        .addComponent(loansTile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(investmentsTile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        homeTilesPanelLayout.setVerticalGroup(
            homeTilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTilesPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(homeTilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(balanceTile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(savingsTile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(homeTilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loansTile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(investmentsTile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Recent Transactions");

        javax.swing.GroupLayout homeTabLayout = new javax.swing.GroupLayout(homeTab);
        homeTab.setLayout(homeTabLayout);
        homeTabLayout.setHorizontalGroup(
            homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTabLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(incomeAndExpenseGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(homeTilesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transactionsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(homeTabLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1)))
                .addGap(70, 70, 70))
        );
        homeTabLayout.setVerticalGroup(
            homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTabLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeTabLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(transactionsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(homeTabLayout.createSequentialGroup()
                        .addComponent(homeTilesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(incomeAndExpenseGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 27, Short.MAX_VALUE))
        );

        transactionsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        masterTabPane.addTab("Home", homeTab);

        linkButton.setText("Launch Link");
        linkButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        linkButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                linkButtonMouseClicked(evt);
            }
        });
        linkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkButtonActionPerformed(evt);
            }
        });

        accountScrollPanel.setBorder(null);

        accountPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accountPanel.setLayout(new java.awt.GridBagLayout());
        accountScrollPanel.setViewportView(accountPanel);

        profileReloadButton.setText("Reload Profile");
        profileReloadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profileReloadButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout accountTabLayout = new javax.swing.GroupLayout(accountTab);
        accountTab.setLayout(accountTabLayout);
        accountTabLayout.setHorizontalGroup(
            accountTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(accountTabLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(accountScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1199, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(accountTabLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(linkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(profileReloadButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        accountTabLayout.setVerticalGroup(
            accountTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountTabLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(accountTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(linkButton)
                    .addComponent(profileReloadButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accountScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE))
        );

        accountScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        profileReloadButton.setVisible(false);

        masterTabPane.addTab("Accounts", accountTab);

        savingsScrollPane.setBorder(null);
        savingsScrollPane.setOpaque(false);
        savingsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        savingsPanel.setOpaque(false);
        savingsPanel.setLayout(new java.awt.GridBagLayout());
        savingsScrollPane.setViewportView(savingsPanel);

        availableAllocationLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        availableAllocationLabel.setText("Available to Allocate");

        actualAllocationLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        actualAllocationLabel.setText("Total Saved");

        availableAmountAllocationLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        availableAmountAllocationLabel.setText("$0.00");

        actualAmountAllocationLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        actualAmountAllocationLabel.setText("$0.00");

        saveGoalEditsButton.setText("Save Edits");
        saveGoalEditsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveGoalEditsButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout goalsAllocationPanelLayout = new javax.swing.GroupLayout(goalsAllocationPanel);
        goalsAllocationPanel.setLayout(goalsAllocationPanelLayout);
        goalsAllocationPanelLayout.setHorizontalGroup(
            goalsAllocationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsAllocationPanelLayout.createSequentialGroup()
                .addGroup(goalsAllocationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(availableAllocationLabel)
                    .addComponent(availableAmountAllocationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(goalsAllocationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(actualAmountAllocationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(actualAllocationLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveGoalEditsButton))
        );
        goalsAllocationPanelLayout.setVerticalGroup(
            goalsAllocationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsAllocationPanelLayout.createSequentialGroup()
                .addGroup(goalsAllocationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableAllocationLabel)
                    .addComponent(actualAllocationLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(goalsAllocationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableAmountAllocationLabel)
                    .addComponent(actualAmountAllocationLabel)
                    .addComponent(saveGoalEditsButton)))
        );

        saveGoalEditsButton.setVisible(false);

        initiateGoalDialogButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        initiateGoalDialogButton.setText("+");
        initiateGoalDialogButton.setAlignmentY(0.0F);
        initiateGoalDialogButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        initiateGoalDialogButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        initiateGoalDialogButton.setIconTextGap(0);
        initiateGoalDialogButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        initiateGoalDialogButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                initiateGoalDialogButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout goalsTitlePanelLayout = new javax.swing.GroupLayout(goalsTitlePanel);
        goalsTitlePanel.setLayout(goalsTitlePanelLayout);
        goalsTitlePanelLayout.setHorizontalGroup(
            goalsTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(initiateGoalDialogButton, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addGap(114, 114, 114))
        );
        goalsTitlePanelLayout.setVerticalGroup(
            goalsTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsTitlePanelLayout.createSequentialGroup()
                .addComponent(initiateGoalDialogButton, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        goalsLabelPanel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        goalsLabelPanel.setText("Goals");

        javax.swing.GroupLayout goalsInfoPanelLayout = new javax.swing.GroupLayout(goalsInfoPanel);
        goalsInfoPanel.setLayout(goalsInfoPanelLayout);
        goalsInfoPanelLayout.setHorizontalGroup(
            goalsInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(goalsInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(goalsAllocationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(goalsInfoPanelLayout.createSequentialGroup()
                        .addComponent(goalsLabelPanel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(goalsTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(372, Short.MAX_VALUE))))
        );
        goalsInfoPanelLayout.setVerticalGroup(
            goalsInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, goalsInfoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(goalsInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(goalsTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(goalsLabelPanel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goalsAllocationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        goalsScrollPane.setBorder(null);
        goalsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        goalsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        goalsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        goalsPanel.setAlignmentX(0.0F);
        goalsPanel.setAlignmentY(0.0F);

        goalsTableScrollPane.setBorder(null);
        goalsTableScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        goalsTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        goalsTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        goalsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Goal", "Saved", "Target", "Begin Date", "Target Date", "Delete?"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TableColumnModel colModel = goalsTable.getColumnModel();

        colModel.getColumn(1).setPreferredWidth(40);
        colModel.getColumn(2).setPreferredWidth(40);
        colModel.getColumn(3).setPreferredWidth(40);
        colModel.getColumn(4).setPreferredWidth(40);
        colModel.getColumn(5).setPreferredWidth(10);

        goalsTable.setIntercellSpacing(new java.awt.Dimension(0, 5));
        goalsTable.setShowHorizontalLines(true);
        goalsTable.getTableHeader().setReorderingAllowed(false);
        goalsTable.setAutoCreateRowSorter(rootPaneCheckingEnabled);
        goalsTable.getTableHeader().setReorderingAllowed(false);
        goalsTable.setRowHeight(30);
        goalsTable.setRowMargin(10);
        goalsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        goalsTableScrollPane.setViewportView(goalsTable);
        // Listen to a user making changes to the table
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener)e.getSource();
                saveGoalEditsButton.setVisible(true);
            }
        };
        TableCellListener tcl = new TableCellListener(goalsTable, action);

        javax.swing.GroupLayout goalsPanelLayout = new javax.swing.GroupLayout(goalsPanel);
        goalsPanel.setLayout(goalsPanelLayout);
        goalsPanelLayout.setHorizontalGroup(
            goalsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(goalsTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        goalsPanelLayout.setVerticalGroup(
            goalsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsPanelLayout.createSequentialGroup()
                .addComponent(goalsTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 103, Short.MAX_VALUE))
        );

        // Send scroll events to outer scroll panel for smooth scrolling
        goalsTableScrollPane.setWheelScrollingEnabled(false);
        goalsTableScrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                goalsScrollPane.dispatchEvent(e);
            }
        });

        goalsScrollPane.setViewportView(goalsPanel);

        savingsQuickHistoryLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        savingsQuickHistoryLabel.setText("Quick History");

        savingsQuickHistoryScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75)));

        savingsQuickHistoryTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        savingsQuickHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Amount", "Balance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        savingsQuickHistoryTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        savingsQuickHistoryTable.setEnabled(false);
        savingsQuickHistoryTable.setRowSelectionAllowed(false);
        savingsQuickHistoryTable.setShowHorizontalLines(true);
        savingsQuickHistoryTable.setIntercellSpacing(new java.awt.Dimension(0, 5));
        savingsQuickHistoryTable.setRowHeight(30);
        savingsQuickHistoryTable.setRowMargin(10);
        savingsQuickHistoryScrollPane.setViewportView(savingsQuickHistoryTable);

        savingsGraph.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75)));
        savingsGraph.setLayout(new javax.swing.BoxLayout(savingsGraph, javax.swing.BoxLayout.PAGE_AXIS));

        javax.swing.GroupLayout savingsTabLayout = new javax.swing.GroupLayout(savingsTab);
        savingsTab.setLayout(savingsTabLayout);
        savingsTabLayout.setHorizontalGroup(
            savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(savingsTabLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(savingsTabLayout.createSequentialGroup()
                        .addComponent(savingsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(savingsTabLayout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(savingsGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(savingsQuickHistoryScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(savingsTabLayout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(savingsQuickHistoryLabel))))
                    .addGroup(savingsTabLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(goalsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(goalsInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        savingsTabLayout.setVerticalGroup(
            savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, savingsTabLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(savingsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(savingsTabLayout.createSequentialGroup()
                        .addComponent(savingsQuickHistoryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(savingsQuickHistoryScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(savingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(savingsTabLayout.createSequentialGroup()
                        .addComponent(goalsInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(goalsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(savingsGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        masterTabPane.addTab("Savings", savingsTab);

        debtsScrollPanel.setBorder(null);

        debtsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        debtsPanel.setLayout(new java.awt.GridBagLayout());
        debtsScrollPanel.setViewportView(debtsPanel);

        javax.swing.GroupLayout debtsTabLayout = new javax.swing.GroupLayout(debtsTab);
        debtsTab.setLayout(debtsTabLayout);
        debtsTabLayout.setHorizontalGroup(
            debtsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1241, Short.MAX_VALUE)
            .addGroup(debtsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(debtsTabLayout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(debtsScrollPanel)
                    .addGap(13, 13, 13)))
        );
        debtsTabLayout.setVerticalGroup(
            debtsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 698, Short.MAX_VALUE)
            .addGroup(debtsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(debtsTabLayout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(debtsScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                    .addGap(24, 24, 24)))
        );

        masterTabPane.addTab("Debts", debtsTab);

        investmentsAccountScrollPanel.setBorder(null);

        investmentsAccountPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        investmentsAccountPanel.setLayout(new java.awt.GridBagLayout());
        investmentsAccountScrollPanel.setViewportView(investmentsAccountPanel);
        investmentsAccountScrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        investmentsPanelStockLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        investmentsPanelStockLabel.setText("Stocks");

        investmentsPanelAddStock.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        investmentsPanelAddStock.setText("+");
        investmentsPanelAddStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        investmentsPanelAddStock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        investmentsPanelAddStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                investmentsPanelAddStockMouseClicked(evt);
            }
        });

        investmentsDisplayStocksScrollPane.setBorder(null);
        investmentsDisplayStocksScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        investmentsDisplayStocksScrollPane.setHorizontalScrollBar(null);
        investmentsDisplayStocksScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        javax.swing.GroupLayout investmentsStockPanelLayout = new javax.swing.GroupLayout(investmentsStockPanel);
        investmentsStockPanel.setLayout(investmentsStockPanelLayout);
        investmentsStockPanelLayout.setHorizontalGroup(
            investmentsStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(investmentsStockPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(investmentsPanelStockLabel)
                .addGap(18, 18, 18)
                .addComponent(investmentsPanelAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(463, Short.MAX_VALUE))
            .addComponent(investmentsDisplayStocksScrollPane)
        );
        investmentsStockPanelLayout.setVerticalGroup(
            investmentsStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(investmentsStockPanelLayout.createSequentialGroup()
                .addGroup(investmentsStockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(investmentsPanelAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(investmentsPanelStockLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(investmentsDisplayStocksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));

        javax.swing.GroupLayout investmentsTabLayout = new javax.swing.GroupLayout(investmentsTab);
        investmentsTab.setLayout(investmentsTabLayout);
        investmentsTabLayout.setHorizontalGroup(
            investmentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(investmentsTabLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(investmentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(investmentsStockPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(investmentsAccountScrollPanel))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        investmentsTabLayout.setVerticalGroup(
            investmentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(investmentsTabLayout.createSequentialGroup()
                .addGroup(investmentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(investmentsTabLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(investmentsAccountScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(investmentsStockPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(investmentsTabLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        masterTabPane.addTab("Investments", investmentsTab);

        spendingGraph.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75), 2));
        spendingGraph.setLayout(new javax.swing.BoxLayout(spendingGraph, javax.swing.BoxLayout.PAGE_AXIS));

        spendingScrollPane.setBorder(null);

        spendingTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75)));
        spendingTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        spendingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Date", "Merchant", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spendingTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        spendingTable.setEnabled(false);
        spendingTable.setRowSelectionAllowed(false);
        spendingTable.setShowHorizontalLines(true);
        TableColumnModel colSpendingModel = spendingTable.getColumnModel();
        colSpendingModel.getColumn(1).setPreferredWidth(30);
        colSpendingModel.getColumn(3).setPreferredWidth(20);

        spendingTable.setIntercellSpacing(new java.awt.Dimension(0, 5));
        spendingTable.setRowHeight(30);
        spendingTable.setRowMargin(10);
        spendingScrollPane.setViewportView(spendingTable);

        spendingLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        spendingLabel.setText("Click on an entry in the graph above to display more details");

        spendingPieChart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75), 2));
        spendingPieChart.setLayout(new javax.swing.BoxLayout(spendingPieChart, javax.swing.BoxLayout.PAGE_AXIS));

        javax.swing.GroupLayout spendingTabLayout = new javax.swing.GroupLayout(spendingTab);
        spendingTab.setLayout(spendingTabLayout);
        spendingTabLayout.setHorizontalGroup(
            spendingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spendingTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(spendingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spendingScrollPane)
                    .addComponent(spendingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addComponent(spendingGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(spendingPieChart, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addGap(40, 40, 40))
        );
        spendingTabLayout.setVerticalGroup(
            spendingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spendingTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(spendingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spendingPieChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spendingGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addComponent(spendingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spendingScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        spendingScrollPane.setVisible(false);

        masterTabPane.addTab("Spending", spendingTab);

        accountDetailsPopup.setBackground(new java.awt.Color(242, 243, 245));
        accountDetailsPopup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75), 2));

        popupAccountName.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        popupAccountName.setText("Bank Account Name");

        popupSeperatorLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        popupSeperatorLabel.setText("<html>&#8226</html>");

        popupAccountType.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        popupAccountType.setText("Bank Account Type");

        popupAccountMaskedNumber.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        popupAccountMaskedNumber.setText("Account Number - Masked");
        popupAccountMaskedNumber.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        popupBalance.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        popupBalance.setText("Account Balance");

        transactionsLabel.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        transactionsLabel.setText("Transactions");

        payBillButton.setVisible(false);
        payBillButton.setText("Pay Bill");
        payBillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payBillButtonActionPerformed(evt);
            }
        });

        availableCreditPanel.setVisible(false);
        availableCreditPanel.setBackground(new java.awt.Color(242, 243, 245));

        availableCreditLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        availableCreditLabel.setText("Available credit");

        popupAvailableCredit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        popupAvailableCredit.setText("$99,999.99");

        javax.swing.GroupLayout availableCreditPanelLayout = new javax.swing.GroupLayout(availableCreditPanel);
        availableCreditPanel.setLayout(availableCreditPanelLayout);
        availableCreditPanelLayout.setHorizontalGroup(
            availableCreditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(availableCreditPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(availableCreditLabel)
                .addGap(4, 4, 4)
                .addComponent(popupAvailableCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        availableCreditPanelLayout.setVerticalGroup(
            availableCreditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(availableCreditPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(availableCreditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableCreditLabel)
                    .addComponent(popupAvailableCredit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        minimumPaymentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75)));
        minimumPaymentPanel.setVisible(false);

        minPaymentLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        minPaymentLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minPaymentLabel.setText("Min. Payment");

        popupMinPayment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        popupMinPayment.setText("$999.99");

        javax.swing.GroupLayout minimumPaymentPanelLayout = new javax.swing.GroupLayout(minimumPaymentPanel);
        minimumPaymentPanel.setLayout(minimumPaymentPanelLayout);
        minimumPaymentPanelLayout.setHorizontalGroup(
            minimumPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, minimumPaymentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(minPaymentLabel)
                .addContainerGap())
            .addGroup(minimumPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(popupMinPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        minimumPaymentPanelLayout.setVerticalGroup(
            minimumPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minimumPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(minPaymentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(popupMinPayment)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dueDatePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75)));
        dueDatePanel.setVisible(false);

        dueDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dueDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dueDateLabel.setText("Due Date");

        popupDueDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        popupDueDate.setText("99/99/9999");

        javax.swing.GroupLayout dueDatePanelLayout = new javax.swing.GroupLayout(dueDatePanel);
        dueDatePanel.setLayout(dueDatePanelLayout);
        dueDatePanelLayout.setHorizontalGroup(
            dueDatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dueDatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dueDatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dueDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(popupDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        dueDatePanelLayout.setVerticalGroup(
            dueDatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dueDatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dueDateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(popupDueDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout accountDetailsPopupLayout = new javax.swing.GroupLayout(accountDetailsPopup);
        accountDetailsPopup.setLayout(accountDetailsPopupLayout);
        accountDetailsPopupLayout.setHorizontalGroup(
            accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                        .addComponent(popupAccountName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(popupSeperatorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(popupAccountType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(popupAccountMaskedNumber))
                    .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                        .addGroup(accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(popupBalance)
                            .addComponent(transactionsLabel)
                            .addComponent(availableCreditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                                .addComponent(minimumPaymentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dueDatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(payBillButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(20, 20, 20))
        );
        accountDetailsPopupLayout.setVerticalGroup(
            accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(popupAccountName)
                    .addComponent(popupSeperatorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(popupAccountType)
                    .addComponent(popupAccountMaskedNumber))
                .addGap(18, 18, 18)
                .addGroup(accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                        .addComponent(popupBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(availableCreditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                        .addGroup(accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dueDatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(minimumPaymentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(accountDetailsPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(accountDetailsPopupLayout.createSequentialGroup()
                        .addComponent(payBillButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountDetailsPopupLayout.createSequentialGroup()
                        .addComponent(transactionsLabel)
                        .addContainerGap())))
        );

        cards.setLayout(new java.awt.CardLayout());

        studentLayoutPanel.setLayout(new java.awt.GridLayout(4, 3, 5, 5));

        studentLabel1.setBackground(new java.awt.Color(225, 225, 225));
        studentLabel1.setText("jLabel1");
        studentLabel1.setOpaque(true);
        studentLayoutPanel.add(studentLabel1);

        studentLabel2.setBackground(new java.awt.Color(225, 225, 225));
        studentLabel2.setText("jLabel2");
        studentLabel2.setOpaque(true);
        studentLayoutPanel.add(studentLabel2);

        studentLabel3.setBackground(new java.awt.Color(225, 225, 225));
        studentLabel3.setText("jLabel3");
        studentLabel3.setOpaque(true);
        studentLayoutPanel.add(studentLabel3);

        studentLabel4.setBackground(new java.awt.Color(242, 243, 245));
        studentLabel4.setText("jLabel4");
        studentLabel4.setOpaque(true);
        studentLayoutPanel.add(studentLabel4);

        studentLabel5.setBackground(new java.awt.Color(242, 243, 245));
        studentLabel5.setText("jLabel5");
        studentLabel5.setOpaque(true);
        studentLayoutPanel.add(studentLabel5);

        studentLabel6.setBackground(new java.awt.Color(242, 243, 245));
        studentLabel6.setText("jLabel6");
        studentLabel6.setOpaque(true);
        studentLayoutPanel.add(studentLabel6);

        studentLabel7.setBackground(new java.awt.Color(225, 225, 225));
        studentLabel7.setText("jLabel7");
        studentLabel7.setOpaque(true);
        studentLayoutPanel.add(studentLabel7);

        studentLabel8.setBackground(new java.awt.Color(225, 225, 225));
        studentLabel8.setText("jLabel8");
        studentLabel8.setOpaque(true);
        studentLayoutPanel.add(studentLabel8);

        studentLabel9.setBackground(new java.awt.Color(225, 225, 225));
        studentLabel9.setText("jLabel9");
        studentLabel9.setOpaque(true);
        studentLayoutPanel.add(studentLabel9);

        studentLabel10.setBackground(new java.awt.Color(242, 243, 245));
        studentLabel10.setText("jLabel10");
        studentLabel10.setOpaque(true);
        studentLayoutPanel.add(studentLabel10);

        studentLabel11.setBackground(new java.awt.Color(242, 243, 245));
        studentLabel11.setText("jLabel11");
        studentLabel11.setOpaque(true);
        studentLayoutPanel.add(studentLabel11);

        studentLabel12.setBackground(new java.awt.Color(242, 243, 245));
        studentLabel12.setText("jLabel12");
        studentLabel12.setOpaque(true);
        studentLayoutPanel.add(studentLabel12);

        javax.swing.GroupLayout studentCardLayout = new javax.swing.GroupLayout(studentCard);
        studentCard.setLayout(studentCardLayout);
        studentCardLayout.setHorizontalGroup(
            studentCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(studentLayoutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE)
        );
        studentCardLayout.setVerticalGroup(
            studentCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(studentLayoutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        cards.add(studentCard, "1");

        transactionsScrollPopup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75), 2));
        transactionsScrollPopup.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        transactionsScrollPopup.setAutoscrolls(true);
        transactionsScrollPopup.setViewportView(transactionsTable);

        transactionsTable.setAutoCreateRowSorter(true);
        transactionsTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        transactionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Merchant", "Category", "Amount"
            }
        ));
        transactionsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        transactionsTable.setEnabled(false);
        transactionsTable.setFillsViewportHeight(true);
        transactionsTable.setRowHeight(30);
        transactionsTable.setRowMargin(10);
        transactionsTable.setShowHorizontalLines(true);
        transactionsScrollPopup.setViewportView(transactionsTable);

        javax.swing.GroupLayout tableCardLayout = new javax.swing.GroupLayout(tableCard);
        tableCard.setLayout(tableCardLayout);
        tableCardLayout.setHorizontalGroup(
            tableCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1121, Short.MAX_VALUE)
            .addGroup(tableCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(transactionsScrollPopup, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1121, Short.MAX_VALUE))
        );
        tableCardLayout.setVerticalGroup(
            tableCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
            .addGroup(tableCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(transactionsScrollPopup, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        cards.add(tableCard, "2");

        mortgageLayoutPanel.setLayout(new java.awt.GridLayout(3, 3, 5, 5));

        mortgageLabel1.setBackground(new java.awt.Color(225, 225, 225));
        mortgageLabel1.setText("jLabel1");
        mortgageLabel1.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel1);

        mortgageLabel2.setBackground(new java.awt.Color(225, 225, 225));
        mortgageLabel2.setText("jLabel2");
        mortgageLabel2.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel2);

        mortgageLabel3.setBackground(new java.awt.Color(225, 225, 225));
        mortgageLabel3.setText("jLabel3");
        mortgageLabel3.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel3);

        mortgageLabel4.setBackground(new java.awt.Color(242, 243, 245));
        mortgageLabel4.setText("jLabel4");
        mortgageLabel4.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel4);

        mortgageLabel5.setBackground(new java.awt.Color(242, 243, 245));
        mortgageLabel5.setText("jLabel5");
        mortgageLabel5.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel5);

        mortgageLabel6.setBackground(new java.awt.Color(242, 243, 245));
        mortgageLabel6.setText("jLabel6");
        mortgageLabel6.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel6);

        mortgageLabel7.setBackground(new java.awt.Color(225, 225, 225));
        mortgageLabel7.setText("jLabel7");
        mortgageLabel7.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel7);

        mortgageLabel8.setBackground(new java.awt.Color(225, 225, 225));
        mortgageLabel8.setText("jLabel8");
        mortgageLabel8.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel8);

        mortgageLabel9.setBackground(new java.awt.Color(225, 225, 225));
        mortgageLabel9.setText("jLabel9");
        mortgageLabel9.setOpaque(true);
        mortgageLayoutPanel.add(mortgageLabel9);

        javax.swing.GroupLayout mortgageCardLayout = new javax.swing.GroupLayout(mortgageCard);
        mortgageCard.setLayout(mortgageCardLayout);
        mortgageCardLayout.setHorizontalGroup(
            mortgageCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1105, Short.MAX_VALUE)
            .addGroup(mortgageCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mortgageLayoutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mortgageCardLayout.setVerticalGroup(
            mortgageCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
            .addGroup(mortgageCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mortgageLayoutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cards.add(mortgageCard, "3");

        javax.swing.GroupLayout accountPopupTabLayout = new javax.swing.GroupLayout(accountPopupTab);
        accountPopupTab.setLayout(accountPopupTabLayout);
        accountPopupTabLayout.setHorizontalGroup(
            accountPopupTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountPopupTabLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(accountPopupTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accountDetailsPopup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );
        accountPopupTabLayout.setVerticalGroup(
            accountPopupTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountPopupTabLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(accountDetailsPopup, 182, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        masterTabPane.addTab("accountPopup", accountPopupTab);
        masterTabPane.remove(6);

        closeAppPanel.setMaximumSize(new java.awt.Dimension(50, 50));
        closeAppPanel.setPreferredSize(new java.awt.Dimension(50, 50));

        closeLabel.setBackground(new java.awt.Color(75, 75, 75));
        closeLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        closeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closeLabel.setText("X");
        closeLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(187, 187, 187)));
        closeLabel.setOpaque(true);
        closeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeLabelMouseExited(evt);
            }
        });

        closeAppPanel.setLayer(closeLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout closeAppPanelLayout = new javax.swing.GroupLayout(closeAppPanel);
        closeAppPanel.setLayout(closeAppPanelLayout);
        closeAppPanelLayout.setHorizontalGroup(
            closeAppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
            .addGroup(closeAppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(closeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
        );
        closeAppPanelLayout.setVerticalGroup(
            closeAppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(closeAppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(closeAppPanelLayout.createSequentialGroup()
                    .addComponent(closeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        accountableHeaderLabel.setFont(new java.awt.Font("sansserif", 0, 28)); // NOI18N
        accountableHeaderLabel.setForeground(new java.awt.Color(0, 0, 0));
        accountableHeaderLabel.setText("AccountAble");

        accountableHeaderPanel.setLayer(accountableHeaderLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout accountableHeaderPanelLayout = new javax.swing.GroupLayout(accountableHeaderPanel);
        accountableHeaderPanel.setLayout(accountableHeaderPanelLayout);
        accountableHeaderPanelLayout.setHorizontalGroup(
            accountableHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountableHeaderPanelLayout.createSequentialGroup()
                .addComponent(accountableHeaderLabel)
                .addGap(0, 1045, Short.MAX_VALUE))
        );
        accountableHeaderPanelLayout.setVerticalGroup(
            accountableHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(accountableHeaderLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accountableHeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addComponent(closeAppPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(masterTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1371, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(accountableHeaderPanel)
                    .addComponent(closeAppPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 698, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 44, Short.MAX_VALUE)
                    .addComponent(masterTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        setSize(new java.awt.Dimension(1371, 748));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void linkButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_linkButtonMouseClicked
        linkButton.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        try {
            String[] command =
            {
                "cmd",
            };
            Process p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
            try (PrintWriter stdin = new PrintWriter(p.getOutputStream())) {
                stdin.println("npx kill-port 3000");
                stdin.println("set REACT_APP_USER_ID=" + QuickstartApplication.userID);
                stdin.println("set REACT_APP_USER_ID");
                stdin.println("cd C:\\Users\\cody6\\Documents\\NetBeansProjects\\AccountAble\\frontend");
                stdin.println("npm start");
            }
            TimeUnit.SECONDS.sleep(6);  // Gives time for the npm script to run and open the html Link Launch
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        linkButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        profileReloadButton.setVisible(true);
    }//GEN-LAST:event_linkButtonMouseClicked

    private void closeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeLabelMouseClicked
        System.exit(0);
    }//GEN-LAST:event_closeLabelMouseClicked

    private void payBillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payBillButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payBillButtonActionPerformed

    private void masterTabPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_masterTabPaneStateChanged
        // clicking out of account details popup closes the tab
        if (masterTabPane.getTabCount() == 7 && masterTabPane.getSelectedIndex() != 6) {
            masterTabPane.remove(6);
        }
    }//GEN-LAST:event_masterTabPaneStateChanged

    private void linkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_linkButtonActionPerformed

    private void balanceTileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_balanceTileMouseClicked
        masterTabPane.setSelectedIndex(1);
    }//GEN-LAST:event_balanceTileMouseClicked

    private void loansTileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loansTileMouseClicked
        masterTabPane.setSelectedIndex(3);
    }//GEN-LAST:event_loansTileMouseClicked

    private void savingsTileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_savingsTileMouseClicked
        masterTabPane.setSelectedIndex(2);
    }//GEN-LAST:event_savingsTileMouseClicked

    private void investmentsTileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_investmentsTileMouseClicked
        masterTabPane.setSelectedIndex(4);
    }//GEN-LAST:event_investmentsTileMouseClicked

    private void initiateGoalDialogButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_initiateGoalDialogButtonMouseClicked
        addGoalDialog.setVisible(true);
    }//GEN-LAST:event_initiateGoalDialogButtonMouseClicked

    private void addGoalButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addGoalButtonMouseClicked
        numberOfSavingsGoals++;

        // add new goal to database
        Map<String, Object> data = new HashMap<>();
        data.put("saved", addGoalAmountSavedField.getText());
        data.put("target", addGoalGoalAmountField.getText());
        data.put("begin", addGoalBeginDateField.getText()); 
        data.put("end", addGoalEndDateField.getText());
        db.collection("users").document(QuickstartApplication.userID).collection("goals").document(addGoalItemDescField.getText()).set(data);

        // Add user-edited fields to table
        DefaultTableModel savingsGoalTableModel = (DefaultTableModel) goalsTable.getModel();
        Object[] row = { addGoalItemDescField.getText(), Double.valueOf(addGoalAmountSavedField.getText()), Double.valueOf(addGoalGoalAmountField.getText()), addGoalBeginDateField.getText(), addGoalEndDateField.getText() };
        savingsGoalTableModel.addRow(row);
        
        // Add new savings amount to allocated savings tracker
        allocatedSavings += Double.valueOf(addGoalAmountSavedField.getText());
        availableAmountAllocationLabel.setText("$" + df.format(allAccountsSavings - allocatedSavings));
        goalsAllocation(goalsAllocationPanel, allAccountsSavings, allocatedSavings);
        
        // Adjust size to only be number of rows
        Dimension size = goalsTable.getPreferredSize();
        int displayRows = Math.min(goalsTable.getRowCount(), numberOfSavingsGoals);
        size.height = displayRows * goalsTable.getRowHeight();
        goalsTable.setPreferredScrollableViewportSize(size);
        goalsTableScrollPane.setViewportView(goalsTable);
        goalsTableScrollPane.revalidate();;
        goalsTableScrollPane.repaint();
        
        // Reset text fields
        addGoalItemDescField.setText("");
        addGoalAmountSavedField.setText("");
        addGoalGoalAmountField.setText("");
        addGoalBeginDateField.setText("");
        addGoalEndDateField.setText("");
        addGoalDialog.setVisible(false);        
    }//GEN-LAST:event_addGoalButtonMouseClicked

    private void saveGoalEditsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveGoalEditsButtonMouseClicked
        int rows = goalsTable.getRowCount();
        
        if (goalsTable.isEditing()) {
            goalsTable.getCellEditor().stopCellEditing();
        }
        
        // Delete row if "Delete" column is checked
        List<Integer> removeFromGoals = new ArrayList <>();
        for (int i = 0; i < rows; i++) {
            if (goalsTable.getModel().getValueAt(i, 5) != null) {
                removeFromGoals.add(0,i);
            }
        }
        for (int row : removeFromGoals) {
            ((DefaultTableModel)goalsTable.getModel()).removeRow(row);
            rows -= removeFromGoals.size();
        }
                    
        try {
            // Delete old savings goals data in database
            ApiFuture<QuerySnapshot> future = db.collection("users").document(QuickstartApplication.userID).collection("goals").get();
            List<QueryDocumentSnapshot> goals = new ArrayList <>();
            goals = future.get().getDocuments();
            for (QueryDocumentSnapshot  goal : goals) {
                db.collection("users").document(QuickstartApplication.userID).collection("goals").document(goal.getId()).delete();
            }
            
            // Add current table to savings goals in database
            for (int insurance = 0; insurance < ((rows + 2) * 2); insurance++) {
                allocatedSavings = 0.00;
                for (int j = 0; j < rows; j++) {
                    Map<String, String> data = new HashMap<>();
                    data.put("saved", goalsTable.getModel().getValueAt(j,1).toString());
                    data.put("target", goalsTable.getModel().getValueAt(j,2).toString());
                    data.put("begin", goalsTable.getModel().getValueAt(j,3).toString());
                    data.put("end", goalsTable.getModel().getValueAt(j,4).toString());
                    allocatedSavings += Double.valueOf(data.get("saved")); // Adjust allocated savings to include new updates
                    // Remove possible breaking char entry
                    String goalItemDesc = goalsTable.getModel().getValueAt(j,0).toString();
                    if (goalItemDesc.contains("/")) {
                        goalItemDesc = goalItemDesc.replaceAll("\\/", "");
                        goalsTable.setValueAt(goalItemDesc, j, 0);
                    }
                    db.collection("users").document(QuickstartApplication.userID).collection("goals").document(goalItemDesc).set(data);
                }
            }
            saveGoalEditsButton.setVisible(false);
            availableAmountAllocationLabel.setText("$" + df.format(allAccountsSavings - allocatedSavings));
            goalsAllocation(goalsAllocationPanel, allAccountsSavings, allocatedSavings);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }//GEN-LAST:event_saveGoalEditsButtonMouseClicked

    private void addGoalItemDescFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addGoalItemDescFieldKeyTyped
        if (evt.getKeyChar() == '/'){
            evt.consume();
        }  
    }//GEN-LAST:event_addGoalItemDescFieldKeyTyped

    private void profileReloadButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_profileReloadButtonMouseClicked
        this.dispose();
        JDialog jDialog = new JDialog();
        jDialog.setLayout(new GridBagLayout());

        JLabel label = new JLabel();
        label.setIcon(new javax.swing.ImageIcon("C:\\Users\\cody6\\Documents\\NetBeansProjects\\AccountAble\\java\\src\\main\\java\\Login\\Spin-1.4s-175px.gif"));

        jDialog.add(label);

        jDialog.setMinimumSize(new Dimension(175, 175));
        jDialog.setResizable(false);
        jDialog.setModal(false);
        jDialog.setUndecorated(true);
        jDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
        
        SwingWorker worker = new SwingWorker(){
            @Override
            protected Void doInBackground() throws Exception {
                new Client().setVisible(true);
                jDialog.dispose();
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_profileReloadButtonMouseClicked

    private void newAccountContinueButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newAccountContinueButtonMouseClicked
        newAccountDialog.dispose();
    }//GEN-LAST:event_newAccountContinueButtonMouseClicked

    private void investmentsPanelAddStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_investmentsPanelAddStockMouseClicked
        stockDialogTickerTextField.setText("");
        stockDialogTickerTextField.setEditable(true);
        stockDialogTickerTextField.setEnabled(true);
        stockDialogSharesTextField.setText("");
        stockDialogCostTextField.setText("");
        stockDialogDateTextField.setText("");
        stockDialogAddOrEditButton.setText("Add");
        stockDialogDeleteButton.setVisible(false);
        addStockDialog.setVisible(true);
    }//GEN-LAST:event_investmentsPanelAddStockMouseClicked

    private void stockDialogAddOrEditButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockDialogAddOrEditButtonMouseClicked
        Map<String, Object> data = new HashMap<>();
        data.put("shares", stockDialogSharesTextField.getText());
        data.put("cost", stockDialogCostTextField.getText());
        data.put("date", stockDialogDateTextField.getText()); 
        db.collection("users").document(QuickstartApplication.userID).collection("stocks").document(stockDialogTickerTextField.getText().toUpperCase()).set(data);
        
        stockDialogTickerTextField.setText("");
        stockDialogTickerTextField.setEditable(true);
        stockDialogTickerTextField.setEnabled(true);
        stockDialogSharesTextField.setText("");
        stockDialogCostTextField.setText("");
        stockDialogDateTextField.setText("");
        
        try {
            stockPanelPopulation();
        } catch (InterruptedException | ExecutionException | IOException | ParseException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        addStockDialog.dispose();
    }//GEN-LAST:event_stockDialogAddOrEditButtonMouseClicked

    private void stockDialogDeleteButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockDialogDeleteButtonMouseClicked
        db.collection("users").document(QuickstartApplication.userID).collection("stocks").document(stockDialogTickerTextField.getText()).delete();
        stockDialogDeleteButton.setVisible(false);
        stockDialogTickerTextField.setText("");
        stockDialogTickerTextField.setEditable(true);
        stockDialogTickerTextField.setEnabled(true);
        stockDialogSharesTextField.setText("");
        stockDialogCostTextField.setText("");
        stockDialogDateTextField.setText("");
        
        try {
            stockPanelPopulation();
        } catch (InterruptedException | ExecutionException | IOException | ParseException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        addStockDialog.dispose();
    }//GEN-LAST:event_stockDialogDeleteButtonMouseClicked

    private void closeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeLabelMouseEntered
        JLabel parent = (JLabel)evt.getSource();
        parent.setBackground(new Color(178,34,34));
        parent.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.revalidate();
    }//GEN-LAST:event_closeLabelMouseEntered

    private void closeLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeLabelMouseExited
        JLabel parent = (JLabel)evt.getSource();
        parent.setBackground(new Color(75, 75, 75));
        parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        parent.revalidate();
    }//GEN-LAST:event_closeLabelMouseExited

    private void stockDialogDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockDialogDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stockDialogDateTextFieldActionPerformed

    private void savingsTableAndGraphHistory() throws ParseException {
        // Allocate savings transactions
        for (Transaction transaction : transactionsList) {
            List<Transaction> tempSavingsList = new ArrayList<>();
            String transactionDate = transaction.getDate().toString();            
            if (savingsAccountList.contains(transaction.getAccountId())) {
                if (savingsAccountTransactionList.containsKey(transactionDate)) {
                    savingsAccountTransactionList.get(transactionDate).add(transaction);
                } else {
                    tempSavingsList.add(transaction);
                    savingsAccountTransactionList.put(transactionDate, tempSavingsList);
                }
            }
        }        
        // SAVINGS TAB table population
        DefaultTableModel model = (DefaultTableModel) savingsQuickHistoryTable.getModel();
        Double savingsBalance = allAccountsSavings;
        TreeMap<LocalDate, Double> savingsGraphData = new TreeMap<>();
        int savingsRowCount = 0;
        for (String date : savingsAccountTransactionList.keySet()) {
            for (Transaction transaction : savingsAccountTransactionList.get(date)) {
                Double transactionAmount = transaction.getAmount();
                savingsGraphData.put(LocalDate.parse(date), savingsBalance);
                Object[] row = { date, df.format(transactionAmount), df.format(savingsBalance) };
                model.addRow(row);
                savingsBalance += transactionAmount;
                if (transactionAmount < 0) {
                    String str = transactionAmount.toString().substring(1);
                    model.setValueAt("+" + str, savingsRowCount, 1);
                }
                savingsRowCount += 1;
            } 
        }
        // Set deposit entries to green
        savingsQuickHistoryTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String str = (String) value;
                if (str.contains("+")) {
                    c.setForeground(new Color(60,179,113));
                }
                else {
                    c.setForeground(new Color(0,0,0));
                }
                return c;
            }
        });
        savingsQuickHistoryScrollPane.setViewportView(savingsQuickHistoryTable);
        savingsQuickHistoryScrollPane.revalidate();
        savingsQuickHistoryScrollPane.repaint();
        
        // Create Savings graph
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        final TimeSeries s1 = new TimeSeries("Series 1");
        for (Map.Entry<LocalDate, Double> entry : savingsGraphData.entrySet()){
            LocalDate ld = entry.getKey();
            Day day = new Day(ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
            s1.add(day, entry.getValue());
        }
        dataset.addSeries(s1);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Savings Balance Over Time", null, "Amount (in USD)", dataset, false, true, false);
        chart.getTitle().setFont(new java.awt.Font("Segoe UI", 0, 18));
        chart.setBackgroundPaint(NORMALTILECOLOR);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYToolTipGenerator xyttg = new StandardXYToolTipGenerator(" {1}: {2} ", DateFormat.getDateInstance(), NumberFormat.getCurrencyInstance());
        plot.getRenderer().setDefaultToolTipGenerator(xyttg);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(true);
        ChartPanel chartPanel = new ChartPanel(chart);
        savingsGraph.setPreferredSize(new Dimension(474, 306));
        savingsGraph.add(chartPanel);
        savingsGraph.revalidate();
        savingsGraph.repaint();
        savingsTab.revalidate();
        savingsTab.repaint();
    }
    
    private void recentTransactionsPopulation() {
        // HOME TAB - Pull all transactions per access token
        for (LocalDate date : datedTransactions.keySet()) {
            if (date.isAfter(java.time.LocalDate.now().minusDays(90)) &&  date.isBefore(java.time.LocalDate.now().plusDays(1))) { // between 3 weeks ago and today
                // Add and set new date label
                JLabel newLabel = new JLabel(date.toString());
                newLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
                newLabel.setAlignmentX(LEFT_ALIGNMENT);
                listedTransactionsPanel.add(newLabel);

                // Add and set new scroll pane container for our table
                JScrollPane recentTransactionScrollPanel = new JScrollPane();
                recentTransactionScrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                recentTransactionScrollPanel.setWheelScrollingEnabled(false);
                // Send scroll events to outer scroll panel for smooth scrolling
                recentTransactionScrollPanel.addMouseWheelListener(new MouseWheelListener() { 
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        transactionsScrollPane.dispatchEvent(e);
                    }
                });
                listedTransactionsPanel.add(recentTransactionScrollPanel);

                // Add and set new JTable
                JTable recentTransactionTable = new JTable();
                recentTransactionTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                    },
                    new String [] {
                        "Merchant", "Category", "Amount"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false
                    };
                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });         
                recentTransactionTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
                newLabel.setAlignmentX(LEFT_ALIGNMENT);
                recentTransactionTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
                recentTransactionTable.setShowHorizontalLines(true);
                recentTransactionTable.getTableHeader().setReorderingAllowed(false);
                recentTransactionTable.setAutoCreateRowSorter(rootPaneCheckingEnabled);
                recentTransactionTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                recentTransactionTable.setRowHeight(30);
                recentTransactionTable.setRowMargin(10);
                recentTransactionTable.setEnabled(false);
                DefaultTableModel datedModel = (DefaultTableModel) recentTransactionTable.getModel();
                int rowCount = 0;
                // Populate date for all of Date's transactions and add to a table
                for (Transaction datedTransaction : datedTransactions.get(date)) {
                    rowCount++;
                    // Pull merchant name
                    String merchantName;
                    String nullNameTest = datedTransaction.getMerchantName();
                    if (nullNameTest == null) {
                        merchantName = datedTransaction.getName();
                    } else {
                        merchantName = nullNameTest;
                    }
                    // Pull category
                    String trueCategory;
                    List<String> transactionCategory = datedTransaction.getCategory();
                    if (transactionCategory.get(1) == null) {
                        if (transactionCategory.get(0) == null){
                            trueCategory = "Category unavailable";
                        } else {
                            trueCategory = transactionCategory.get(0);
                        }
                    } else {
                        trueCategory = transactionCategory.get(1);
                    }
                    // Pull transaction amount
                    String transactionAmount = df.format(datedTransaction.getAmount());
                    // add transaction to this date's table
                    Object[] row = { merchantName, trueCategory, transactionAmount };
                    datedModel.addRow(row);
                }
                // Adjust tables to only be size of the number of rows
                Dimension size = recentTransactionTable.getPreferredSize();
                int displayRows = Math.min(recentTransactionTable.getRowCount(), rowCount);
                size.height = displayRows * recentTransactionTable.getRowHeight();
                recentTransactionTable.setPreferredScrollableViewportSize(size);
                // Add table to panel
                recentTransactionScrollPanel.setSize(size);
                recentTransactionScrollPanel.setViewportView(recentTransactionTable);
                // Add spacer after Date's entry
                listedTransactionsPanel.add(Box.createRigidArea(new Dimension(0,15)));
            }   
        }
    }
  
    private void accountPanelPopulation() {
        // Pull basic account information and display it in tiles
        for (AccountBase account : accountsList) {
            // Getting row informations
            String accountName = account.getName();
            String accountSubType;
            AccountSubtype nullTypeTest = account.getSubtype();
             if (nullTypeTest == null) {
                accountSubType = account.getType().toString();
            } else {
                accountSubType = nullTypeTest.toString();
            }               
            Double accountCurrent;
            Double nullCurrentTest = account.getBalances().getCurrent();  
            Double nullAvailableTest = account.getBalances().getAvailable();
            if (nullCurrentTest == null) {
                if (nullAvailableTest == null) {
                    accountCurrent = 0.00;
                } else {
                    accountCurrent = nullAvailableTest;
                }
            } else {
                accountCurrent = nullCurrentTest;
            }
            String accountMask;
            String nullMaskTest = account.getMask();
            if (nullMaskTest == null) {
                accountMask = "****";
            } else {
                accountMask = nullMaskTest;
            }
            // Setup various tiles going to account and other tabs  
            JPanel accountTile = new javax.swing.JPanel();
            accountTile.setBackground(NORMALTILECOLOR);
            JPanel savingsTile = new javax.swing.JPanel();
            savingsTile.setBackground(NORMALTILECOLOR);
            JPanel debtsTile = new javax.swing.JPanel();
            debtsTile.setBackground(NORMALTILECOLOR);
            JPanel investmentsTile = new javax.swing.JPanel();
            investmentsTile.setBackground(NORMALTILECOLOR);
            List<JPanel> tileList = new ArrayList<>();
            tileList.add(accountTile);
            if (savingsList.contains(accountSubType)) {
                tileList.add(savingsTile);
            }
            if (creditList.contains(accountSubType) || loanList.contains(accountSubType)) {
                tileList.add(debtsTile);
            }
            if (investmentList.contains(accountSubType)) {
                tileList.add(investmentsTile);
            }
            // Set format for tiles
            for (JPanel tile : tileList) {
                JLabel nameLabel = new JLabel(accountName);
                nameLabel.setFont(new java.awt.Font("Segoe UI", 0, 24));
                JLabel subTypeLabel = new JLabel(accountSubType);
                subTypeLabel.setFont(new java.awt.Font("Segoe UI", 0, 16));
                JLabel currentAmountLabel = new JLabel("$" + df.format(accountCurrent));
                currentAmountLabel.setFont(new java.awt.Font("Segoe UI", 0, 20));

                tile.setLayout(new javax.swing.BoxLayout(tile, javax.swing.BoxLayout.PAGE_AXIS));
                tile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                tile.setFocusable(true);
                tile.add(Box.createRigidArea(new Dimension(15,10)));
                tile.add(nameLabel);
                tile.add(subTypeLabel);
                tile.add(Box.createRigidArea(new Dimension(15,20)));
                tile.add(currentAmountLabel);
                tile.setPreferredSize(new Dimension(180,145));
            }
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(15, 15, 0, 0);
            c.weightx = 0.001;
            c.weighty = 0.001;                
            c.gridx = accountIteratorXAxis;
            c.gridy = accountIteratorYAxis;
            accountIteratorXAxis++;               
            if (accountIteratorXAxis == 5) {   // move to next row
                c.insets = new Insets(15, 15, 0, 30);
                accountIteratorXAxis = 0;
                accountIteratorYAxis++;
            }                
            // Write copy to "Accounts" for every account
            accountPanel.add(accountTile, c);
            accountPanel.revalidate();
            accountPanel.repaint();
            // Depository-type accounts stay in "Accounts" tab
            if (depositoryList.contains(accountSubType)) {
                addClickForDetails(accountTile, accountName, accountSubType, accountCurrent, accountMask, account);
                tileClickColorChange(accountTile);
                allAccountBalance += accountCurrent; // add to overall account balance
            }
            // Savings-type accounts get added to "Accounts" and to "Savings" tab
            else if (savingsList.contains(accountSubType)) {
                savingsAccountList.add(account.getAccountId());
                c.gridx = savingsIteratorXAxis;
                c.gridy = savingsIteratorYAxis;
                savingsIteratorXAxis++;        
                c.insets = new Insets(15, 15, 0, 0);
                if (savingsIteratorXAxis == 2) {   // move to next row
                    c.insets = new Insets(15, 15, 0, 30);
                    savingsIteratorXAxis = 0;
                    savingsIteratorYAxis++;
                }       
                addClickForDetails(savingsTile, accountName, accountSubType, accountCurrent, accountMask, account);
                addClickForDetails(accountTile, accountName, accountSubType, accountCurrent, accountMask, account);
                tileClickColorChange(savingsTile);
                tileClickColorChange(accountTile);
                savingsPanel.add(savingsTile, c);
                savingsPanel.revalidate();
                savingsPanel.repaint(); 
                allAccountsSavings += accountCurrent;
            }
            // Credit-type accounts get added to "Accounts" and to "Debts" tab
            else if (creditList.contains(accountSubType) || loanList.contains(accountSubType)) {
                c.gridx = debtsIteratorXAxis;
                c.gridy = debtsIteratorYAxis;
                debtsIteratorXAxis++;               
                if (debtsIteratorXAxis == 2) {   // move to next row
                    c.insets = new Insets(15, 15, 0, 30);
                    debtsIteratorXAxis = 0;
                    debtsIteratorYAxis++;
                }           
                addClickForDetails(debtsTile, accountName, accountSubType, accountCurrent, accountMask, account);
                addClickForDetails(accountTile, accountName, accountSubType, accountCurrent, accountMask, account);
                tileClickColorChange(debtsTile);
                tileClickColorChange(accountTile);
                debtsPanel.add(debtsTile, c);
                debtsPanel.revalidate();
                debtsPanel.repaint(); 
                if (creditList.contains(accountSubType)) {
                    allAccountBalance -= accountCurrent;
                } else {
                    allAccountsLoans += accountCurrent;
                }
            }
            // Investment-type accounts get added to "Accounts" and to "Investments" tab, but no clickable details
            else if (investmentList.contains(accountSubType)) {
                c.gridx = investmentIteratorXAxis;
                c.gridy = investmentIteratorYAxis;
                investmentIteratorXAxis++;               
                if (investmentIteratorXAxis == 2) {   // move to next row
                    c.insets = new Insets(15, 15, 0, 30);
                    investmentIteratorXAxis = 0;
                    investmentIteratorYAxis++;
                }                
                investmentsAccountPanel.add(investmentsTile, c);
                investmentsAccountPanel.revalidate();
                investmentsAccountPanel.repaint(); 
                allAccountsInvestments += accountCurrent; 
            }                
        }
        // add empty row at end of "Accounts" for formatting
        for (int k = 0; k < 5; k++) {
            JPanel accountTile = new javax.swing.JPanel();
            accountTile.setPreferredSize(new Dimension(180,145));
            accountTile.setBorder(null);
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(15, 15, 0, 0);
            if (k == 4) {
                c.insets = new Insets(15, 15, 0, 30);
            }
            c.weightx = 0.001;
            c.weighty = 0.001;                
            c.gridx = k;
            c.gridy = accountIteratorYAxis + 1;
            accountPanel.add(accountTile, c);
            accountPanel.revalidate();
            accountPanel.repaint();
        }
        // add empty tile at end of "Savings", "Debts" and "Investments" tabs if only 1 tile in final row
        if (savingsIteratorXAxis == 1) {
            JPanel fillerTile = new javax.swing.JPanel();
            fillerTile.setBorder(null);
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(15, 15, 0, 0);
            c.weightx = 0.001;
            c.weighty = 0.001;                
            c.gridx = savingsIteratorXAxis;
            c.gridy = savingsIteratorYAxis;
            savingsPanel.add(savingsTile, c);
        }
        totalBalance.setText("$" + df.format(allAccountBalance));
        totalLoans.setText("$" + df.format(allAccountsLoans));
        totalSavings.setText("$" + df.format(allAccountsSavings));
        totalInvestments.setText("$" + df.format(allAccountsInvestments));
        balanceTile.revalidate();
        balanceTile.repaint();
    }      
    
    private void tileClickColorChange(JPanel tileName) {
        tileName.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tileName.setBackground(ONCLICKTILECOLOR);
            }
        });   
        tileName.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {       
                tileName.setBackground(NORMALTILECOLOR);
            }
        });
        tileName.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {       
                tileName.setBackground(HOVERCOLOR);
                tileName.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        tileName.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {       
                tileName.setBackground(NORMALTILECOLOR);
                tileName.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    private void goalsAllocation(JPanel goalsAllocationPanel, double allAccountsSavings, double allocatedSavings) {
        if (allAccountsSavings < allocatedSavings) {
            goalsAllocationPanel.setBackground(new java.awt.Color(255,51,51));
            goalsAllocationPanel.setToolTipText("More funds have been allocated than what is available");
            UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 14));
            
        } else {
            goalsAllocationPanel.setBackground(new java.awt.Color(214,217,223));
            goalsAllocationPanel.setToolTipText(null);
        }
    }
    
    private void addClickForDetails(JPanel tileName, String accountName, String accountSubType, Double accountCurrent, String accountMask, AccountBase account) {
        // ira and 401k, credit card, mortgage, student loan are different
        CardLayout cardLayout = (CardLayout)(cards.getLayout());
        if (accountSubType.equals("student")) {       
            tileName.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {  
                    // shorten account name if needed and assign to tab name
                    if (accountName.length() >= 11) {
                        String newName = accountName.substring(0,10);
                        masterTabPane.addTab(newName + "...", accountPopupTab);
                    } else {
                        masterTabPane.addTab(accountName, accountPopupTab);
                    }

                    // Assign basic acocunt info, hide credit card panels
                    popupAccountName.setText(accountName);
                    popupAccountType.setText(accountSubType);
                    popupBalance.setText("$" + df.format(accountCurrent));
                    transactionsLabel.setText("Loan Information");
                    popupAccountMaskedNumber.setText("********" + accountMask);
                    availableCreditPanel.setVisible(false);
                    minimumPaymentPanel.setVisible(false);
                    dueDatePanel.setVisible(false);

                    // Pull student liability info and assign to student grid card
                    List<StudentLoan> studentLoans = new ArrayList <>();
                    for (LiabilitiesObject liability : liabilitiesList) {
                        studentLoans.addAll(liability.getStudent());
                    }
                    for (StudentLoan loan : studentLoans) {
                        if (account.getAccountId().equals(loan.getAccountId())) {
                            studentLabel1.setText("Original Principal Amount: $" + df.format(loan.getOriginationPrincipalAmount()));
                            studentLabel2.setText("Interest Rate: " + loan.getInterestRatePercentage() + "%");                               
                            studentLabel3.setText("Outstanding Interest: $" + df.format(loan.getOutstandingInterestAmount()));                                
                            studentLabel4.setText("Payments Made: " + loan.getPslfStatus().getPaymentsMade().intValue());                                
                            studentLabel5.setText("Payments Remaining: " + loan.getPslfStatus().getPaymentsRemaining().intValue());                               
                            studentLabel6.setText("Expected Payoff Date: " + loan.getExpectedPayoffDate());                               
                            studentLabel7.setText("Last Payment Amount: $" + df.format(loan.getLastPaymentAmount()));                                
                            studentLabel8.setText("Last Payment Date: " + loan.getLastPaymentDate());                               
                            studentLabel9.setText("Next Payment Due Date: " + loan.getNextPaymentDueDate());                               
                            studentLabel10.setText("Min. Payment Amount: $" + df.format(loan.getMinimumPaymentAmount()));                                
                            studentLabel11.setText("YTD Interest Paid: $" + df.format(loan.getYtdInterestPaid()));
                            studentLabel12.setText("YTD Principal Paid: $" + df.format(loan.getYtdPrincipalPaid()));
                            JLabel labelList[] = new JLabel[]{ studentLabel1, studentLabel2, studentLabel3, studentLabel4, studentLabel5, studentLabel6, studentLabel7, studentLabel8, studentLabel9, studentLabel10, studentLabel11, studentLabel12 };
                            for (int i = 0; i < 12; i++) {
                                labelList[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                                labelList[i].setVerticalAlignment(javax.swing.SwingConstants.CENTER);
                                labelList[i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                                labelList[i].setFont(new java.awt.Font("Segoe UI", 0, 20));
                                labelList[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75), 2));
                            }
                        }
                    }
                    // Pull up student card and snap to account pane
                    cardLayout.show(cards, "1");
                    accountPopupTab.revalidate();
                    accountPopupTab.repaint();
                    masterTabPane.setSelectedIndex(6);
                    
                }   
            });
        }
        else if (accountSubType.equals("mortgage")) {       
            tileName.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {  
                    // shorten account name if needed and assign to tab name
                    if (accountName.length() >= 11) {
                        String newName = accountName.substring(0,10);
                        masterTabPane.addTab(newName + "...", accountPopupTab);
                    } else {
                        masterTabPane.addTab(accountName, accountPopupTab);
                    }

                    // Assign basic acocunt info, hide credit card panels
                    popupAccountName.setText(accountName);
                    popupAccountType.setText(accountSubType);
                    popupBalance.setText("$" + df.format(accountCurrent));
                    transactionsLabel.setText("Loan Information");
                    popupAccountMaskedNumber.setText("********" + accountMask);
                    availableCreditPanel.setVisible(false);
                    minimumPaymentPanel.setVisible(false);
                    dueDatePanel.setVisible(false);

                    // Pull mortgage liability info and assign to mortgage grid card
                    List<MortgageLiability> mortgageLoans = new ArrayList <>();
                    for (LiabilitiesObject liability : liabilitiesList) {
                        mortgageLoans.addAll(liability.getMortgage());
                    }
                    for (MortgageLiability loan : mortgageLoans) {
                        if (account.getAccountId().equals(loan.getAccountId())) {
                            mortgageLabel1.setText("Original Principal Amount: $" + df.format(loan.getOriginationPrincipalAmount()));
                            mortgageLabel2.setText("Interest Rate: " + df.format(loan.getInterestRate().getPercentage()) + "%");                               
                            mortgageLabel3.setText("Loan Type: " + loan.getLoanTerm() + " " + loan.getInterestRate().getType());                                                             
                            mortgageLabel4.setText("Last Payment Amount: $" + df.format(loan.getLastPaymentAmount()));                                
                            mortgageLabel5.setText("Last Payment Date: " + loan.getLastPaymentDate());                               
                            mortgageLabel6.setText("YTD Interest Paid: $" + df.format(loan.getYtdInterestPaid()));                       
                            mortgageLabel7.setText("Next Payment Amount: $" + df.format(loan.getNextMonthlyPayment()));                                
                            mortgageLabel8.setText("Next Payment Due Date: " + loan.getNextPaymentDueDate());  
                            mortgageLabel9.setText("YTD Principal Paid: $" + df.format(loan.getYtdPrincipalPaid()));
                            JLabel labelList[] = new JLabel[]{ mortgageLabel1, mortgageLabel2, mortgageLabel3, mortgageLabel4, mortgageLabel5, mortgageLabel6, mortgageLabel7, mortgageLabel8, mortgageLabel9 };
                            for (int i = 0; i < 9; i++) {
                                labelList[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                                labelList[i].setVerticalAlignment(javax.swing.SwingConstants.CENTER);
                                labelList[i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                                labelList[i].setFont(new java.awt.Font("Segoe UI", 0, 20));
                                labelList[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 75), 2));
                            }
                        }
                    }
                    // Pull up mortgage card and snap to account pane
                    cardLayout.show(cards, "3");
                    accountPopupTab.revalidate();
                    accountPopupTab.repaint();
                    masterTabPane.setSelectedIndex(6);
                }   
            });
        } else {
            tileName.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {  
                    
                    // shorten account name if needed and assign to tab name
                    if (accountName.length() >= 11) {
                        String newName = accountName.substring(0,10);
                        masterTabPane.addTab(newName + "...", accountPopupTab);
                    } else {
                        masterTabPane.addTab(accountName, accountPopupTab);
                    }

                    // Assign basic acocunt info, hide credit card panels
                    popupAccountName.setText(accountName);
                    popupAccountType.setText(accountSubType);
                    popupBalance.setText("$" + df.format(accountCurrent));
                    transactionsLabel.setText("Transactions");
                    popupAccountMaskedNumber.setText("********" + accountMask);
                    availableCreditPanel.setVisible(false);
                    minimumPaymentPanel.setVisible(false);
                    dueDatePanel.setVisible(false);

                    // Credit card case to add extra details on limit and payments
                    if (accountSubType.equals("credit card")) {
                        // Make credit panels visible
                        availableCreditPanel.setVisible(true);
                        minimumPaymentPanel.setVisible(true);
                        dueDatePanel.setVisible(true);

                        // Pull liability info and assign
                        List<CreditCardLiability> creditCardLiabilities = new ArrayList <>();
                        for (LiabilitiesObject liability : liabilitiesList) {
                            creditCardLiabilities.addAll(liability.getCredit());
                        }
                        for (CreditCardLiability liability : creditCardLiabilities) {
                            if (account.getAccountId().equals(liability.getAccountId())) {
                                popupAvailableCredit.setText("$" + df.format(account.getBalances().getLimit()-account.getBalances().getCurrent()).toString());
                                popupMinPayment.setText("$" + df.format(liability.getMinimumPaymentAmount()).toString());
                                popupDueDate.setText(liability.getNextPaymentDueDate().toString());
                            }
                        }
                    }

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //////////////////////////////////Add case to add interest rates for savings accounts - where credit limit goes//////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    // Pull transaction details for the account and place into table
                    DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
                    model.setRowCount(0);
                    TableColumnModel colModel = transactionsTable.getColumnModel();
                    for (Transaction transaction : transactionsList) {
                        if (account.getAccountId().equals(transaction.getAccountId())) {
                            String transactionDate = transaction.getDate().toString();
                            String merchantName = transaction.getName();
                            String transactionAmount = df.format(transaction.getAmount());
                            String trueCategory = "";
                            List<String> transactionCategory = transaction.getCategory();
                            if (transactionCategory.get(1) == null) {
                                if (transactionCategory.get(0) == null){
                                    trueCategory = "Category can not be found";
                                } else {
                                    trueCategory = transactionCategory.get(0);
                                }
                            } else {
                                trueCategory = transactionCategory.get(1);
                            }
                            Object[] row = { transactionDate, merchantName, trueCategory, "$" + transactionAmount };
                            model.addRow(row);
                        }
                    }
                    
                    // Change to transaction card, snap view to pane, adjust size of columns
                    cardLayout.show(cards, "2");
                    cards.revalidate();
                    cards.repaint();
                    masterTabPane.setSelectedIndex(6);
                    colModel.getColumn(0).setPreferredWidth(175);
                    colModel.getColumn(1).setPreferredWidth(425);
                    colModel.getColumn(2).setPreferredWidth(325);
                    colModel.getColumn(3).setPreferredWidth(170);
                }   
            });
        }
    }
    
    private void savingsGoals() throws InterruptedException, ExecutionException {
        // Keep savings goals in database, display on client opening
        ApiFuture<QuerySnapshot> future = db.collection("users").document(QuickstartApplication.userID).collection("goals").get();
        List<QueryDocumentSnapshot> goals = future.get().getDocuments();
        for (QueryDocumentSnapshot  goal : goals) {
            numberOfSavingsGoals++;
            allocatedSavings += Double.valueOf(goal.get("saved").toString());
            DefaultTableModel savingsGoalTableModel = (DefaultTableModel) goalsTable.getModel();
            Object[] row = { goal.getId(), Double.valueOf(goal.get("saved").toString()), Double.valueOf(goal.get("target").toString()), goal.get("begin").toString(), goal.get("end").toString() };
            savingsGoalTableModel.addRow(row);
            // Adjust size to only be number of rows
            Dimension size = goalsTable.getPreferredSize();
            int displayRows = Math.min(goalsTable.getRowCount(), numberOfSavingsGoals);
            size.height = displayRows * goalsTable.getRowHeight();
            goalsTable.setPreferredScrollableViewportSize(size);
        }
        goalsTableScrollPane.setViewportView(goalsTable);
        goalsTableScrollPane.revalidate();
        goalsTableScrollPane.repaint();
        availableAmountAllocationLabel.setText("$" + df.format(allAccountsSavings - allocatedSavings));
        actualAmountAllocationLabel.setText("$" + df.format(allAccountsSavings));
        goalsAllocation(goalsAllocationPanel, allAccountsSavings, allocatedSavings);
    }

    private void createHomeTabChart() {
        final int numberOfMonths = 6;
        // Get monthly value of desired months
        Map<String, Double[]> monthlyTotals = new LinkedHashMap<String, Double[]>();  // { incoming[0], outgoing[1] }
        String[] months = new String[numberOfMonths];
        LocalDate month = LocalDate.now().minusMonths(numberOfMonths);
        Double[] emptyArray = new Double[] {0.00, 0.00};
        for (int i = numberOfMonths - 1; i >= 0; i--) {
            month = month.plusMonths(1);
            months[i] = ((month.withDayOfMonth(1)).toString()); 
            monthlyTotals.put(months[i], emptyArray);
        }
        // Pull transaction amounts for incoming and outgoing monies
        for (Transaction transaction : transactionsList) {
            //if (accountIdList.contains(transaction.getAccountId())) {
                String transactionDate = (transaction.getDate().withDayOfMonth(1)).toString();
                Double transactionAmount = transaction.getAmount();
                // check transDate to see if within desired range, will add to monthlyTotals map
                for (int i = 0; i < numberOfMonths; i++) {
                    if (transactionDate.equals(months[i])){
                        Double[] totals = new Double[] {0.00, 0.00};
                        Double[] oldAmounts = monthlyTotals.get(months[i]);
                        if (transactionAmount < 0) {    // negative amount transactions are incoming monies
                            Double[] newAmounts = new Double[] {transactionAmount, 0.00};
                            totals[0] = oldAmounts[0] + newAmounts[0];
                            totals[1] = oldAmounts[1] + newAmounts[1];
                        } else {                        // positive amount transactions are outgoing monies
                            Double[] newAmounts = new Double[] {0.00, transactionAmount};
                            totals[0] = oldAmounts[0] + newAmounts[0];
                            totals[1] = oldAmounts[1] + newAmounts[1];
                        }
                        monthlyTotals.put(months[i], totals);
                        break;
                    }
                }
            //}
        }
        // fill out dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double[]> entry : monthlyTotals.entrySet()) {
            String monthName = entry.getKey();
            String yAxisName = monthName.substring(0, monthName.length() - 3);
            Double[] totals = entry.getValue();
            dataset.addValue(totals[0] * -1, "Income", yAxisName);
            dataset.addValue(totals[1], "Spending", yAxisName);
        }

        JFreeChart chart  = ChartFactory.createBarChart("Income vs. Spending","Month", "Amount (in USD)", dataset,PlotOrientation.VERTICAL,true, true,false);
        chart.getTitle().setFont(new java.awt.Font("Segoe UI", 0, 18));
        chart.addSubtitle(new TextTitle("Past " + numberOfMonths + " Months"));
        chart.setBackgroundPaint(NORMALTILECOLOR);
        LegendTitle legend = chart.getLegend();
        legend.setBackgroundPaint(NORMALTILECOLOR);
        CategoryPlot plot = chart.getCategoryPlot();
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
        CategoryItemRenderer cir = plot.getRenderer();
        cir.setSeriesPaint(0, new Color(60,179,113)); // income
        cir.setSeriesPaint(1, new Color(178,34,34)); // spending
        ChartPanel chartPanel = new ChartPanel(chart);
        incomeAndExpenseGraph.setPreferredSize(new Dimension(472, 403));
        incomeAndExpenseGraph.add(chartPanel);
        incomeAndExpenseGraph.revalidate();
        incomeAndExpenseGraph.repaint();
    }
    
    private void spendingTableAndGraphHistory() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<LocalDate, List<Transaction>> weeklyTransactions = new TreeMap<>(Collections.reverseOrder());
        LocalDate today = LocalDate.now().minusWeeks(spendingNumberOfWeeks);
        
        // Create a list of weekly periods as per spendingNumberOfWeeks
        for (int i = 0; i < spendingNumberOfWeeks; i++) {
            DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
            DayOfWeek lastDayOfWeek = firstDayOfWeek.plus(6);
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
            today = today.plusWeeks(1);
            Double weeklyAmount = 0.00;
           // If transaction is within weekly period, is an outgoing transaction, and is not a credit card payment, add to weekly transaction map.
            for (Map.Entry<LocalDate, List<Transaction>> entry : datedTransactions.entrySet()) {
                if (entry.getKey().isAfter(startOfWeek) && entry.getKey().isBefore(endOfWeek)) {
                    List<Transaction> tempList = new ArrayList<>();
                    for (Transaction transaction : entry.getValue()) {
                        List<String> tempCategory = transaction.getCategory();
                        if (transaction.getAmount() > 0 && !tempCategory.get(1).equalsIgnoreCase("credit card")) { // removes incoming transactions, and payments to credit cards (redundant)
                            weeklyAmount = weeklyAmount + transaction.getAmount(); 
                            tempList.add(transaction);
                        }
                    }
                    if (weeklyTransactions.containsKey(startOfWeek)) {
                        weeklyTransactions.get(startOfWeek).addAll(tempList);
                    } else {
                        weeklyTransactions.put(startOfWeek, tempList);
                    }
                }
            }
            dataset.addValue(weeklyAmount, "Spending", startOfWeek.toString());          
        }
        
        // Create bar chart
        JFreeChart chart  = ChartFactory.createBarChart("Weekly Spending", null, "Amount (in USD)", dataset, PlotOrientation.VERTICAL, false, true, false);
        chart.getTitle().setFont(new java.awt.Font("Segoe UI", 0, 18));
        chart.addSubtitle(new TextTitle("Past " + spendingNumberOfWeeks + " Weeks"));
        chart.setBackgroundPaint(NORMALTILECOLOR);
        CategoryPlot plot = chart.getCategoryPlot();
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
        CategoryItemRenderer cir = plot.getRenderer();
        cir.setSeriesPaint(0, new Color(178,34,34)); // spending
        CategoryAxis domainAxis = plot.getDomainAxis(); 
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);  
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // Add mouse listeners to chart to display details of a weekly period
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                ChartEntity entity = event.getEntity();
                LocalDate date = null;
                if (entity instanceof CategoryItemEntity) { 
                    CategoryItemEntity itemEntity = (CategoryItemEntity) entity;
                    date = LocalDate.parse(itemEntity.getColumnKey().toString());
                }
                else if (entity instanceof CategoryLabelEntity) { 
                    CategoryLabelEntity labelEntity = (CategoryLabelEntity) entity;
                    date = LocalDate.parse(labelEntity.getKey().toString());                  
                }                
                if (date != null) {
                    DefaultTableModel model = (DefaultTableModel) spendingTable.getModel();
                    model.setRowCount(0);
                    int spendingRowCount = 0;
                    if (weeklyTransactions.get(date) != null) {
                        for (Transaction transaction : weeklyTransactions.get(date)) {
                            String trueCategory = "Category unavailable";
                            List<String> transactionCategory = transaction.getCategory();
                            if (transactionCategory.size() != 0) {
                                trueCategory = transactionCategory.get(transactionCategory.size() - 1);
                            }
                            String merchantName;
                            String nullNameTest = transaction.getMerchantName();
                            if (nullNameTest == null) {
                                merchantName = transaction.getName();
                            } else {
                                merchantName = nullNameTest;
                            }
                            Double transactionAmount = transaction.getAmount();
                            Object[] row = { trueCategory, transaction.getDate().toString(), merchantName, df.format(transactionAmount) };
                            model.addRow(row);
                            spendingRowCount += 1;   
                        }
                    }
                    Dimension size = spendingTable.getPreferredSize();
                    int displayRows = Math.min(spendingTable.getRowCount(), spendingRowCount);
                    size.height = displayRows * spendingTable.getRowHeight();
                    spendingTable.setPreferredScrollableViewportSize(size);
                    if (spendingRowCount == 0) {
                        spendingScrollPane.setVisible(false);
                        spendingLabel.setText("No outgoing transactions from " + date + " to " + date.plusDays(6));
                        if (!isBasicCategoryChart) {
                            spendingCategoryChart(startDateBasic, endDateBasic, "Past " + spendingNumberOfWeeks + " Weeks");
                            isBasicCategoryChart = true;
                        }
                    } else {
                        spendingScrollPane.setVisible(true);
                        spendingLabel.setText("Details from " + date + " to " + date.plusDays(6));
                        spendingCategoryChart(date, date.plusDays(6), "Details from " + date + " to " + date.plusDays(6));
                        isBasicCategoryChart = false;
                    }
                }
                else {
                    spendingScrollPane.setVisible(false);
                    spendingLabel.setText("Click an entry in the graph above to display more details");
                    if (!isBasicCategoryChart) {
                        spendingCategoryChart(startDateBasic, endDateBasic, "Past " + spendingNumberOfWeeks + " Weeks");
                        isBasicCategoryChart = true;
                    }
                }          
            }
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////// Possible addition - highlight entity on hover //////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                ChartEntity entity = event.getEntity();
                if (entity instanceof CategoryItemEntity) { //CategoryLabelEntity
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            
        });
        spendingGraph.setPreferredSize(new Dimension(650, 300));
        spendingGraph.add(chartPanel);
        spendingGraph.revalidate();
        spendingGraph.repaint();
        spendingTable.revalidate();
        spendingTable.repaint(); 
    }
    
    private void spendingCategoryChart(LocalDate startDate, LocalDate endDate, String subtitleString) {
        // Create map of category + transactions
        Map<String, List<Transaction>> spendingTransactionsByCategory = new TreeMap<>();
        for (Map.Entry<LocalDate, List<Transaction>> entry : datedTransactions.entrySet()) {
            if (entry.getKey().isAfter(startDate) && entry.getKey().isBefore(endDate)) {
                for (Transaction transaction : entry.getValue()) {
                    List<String> categoryList = transaction.getCategory();
                    if (transaction.getAmount() > 0 && !categoryList.get(1).equalsIgnoreCase("credit card")) { // removes incoming transactions, and payments to credit cards (redundant)
                        List<Transaction> tempList = new ArrayList <>();
                        tempList.add(transaction);
                        if (spendingTransactionsByCategory.containsKey(categoryList.get(0))) {
                            spendingTransactionsByCategory.get(categoryList.get(0)).addAll(tempList);
                        } else {
                            spendingTransactionsByCategory.put(categoryList.get(0), tempList);
                        }
                    }
                }
            }
        }
        
        // Read category map and add to dataset
        DefaultPieDataset dataset = new DefaultPieDataset(); 
        Map<String, Map<String, Double>> categoryMap = new TreeMap<>();
        // map of highest level category : [map of most specific category : amount]
        for (Map.Entry<String, List<Transaction>> entry : spendingTransactionsByCategory.entrySet()) {
            Map<String, Double> subcategoryMap = new TreeMap<>();
            Double categoryAmount = 0.00;
            for (Transaction transaction : entry.getValue()) {
                List<String> categoryList = transaction.getCategory();
                int size = categoryList.size();
                // get category amount and update total for dataset
                categoryAmount = categoryAmount + transaction.getAmount();
                // get subcategory tied to category and update amount within subcategory map
                String subcategory = categoryList.get(size - 1);
                Double subcategoryAmount = transaction.getAmount();
                if (subcategoryMap.containsKey(subcategory)) {
                    Double existingAmount = subcategoryMap.get(subcategory);
                    subcategoryMap.put(subcategory, subcategoryAmount + existingAmount);
                } else {
                    subcategoryMap.put(subcategory, subcategoryAmount);
                }
            }
            dataset.setValue(entry.getKey(), categoryAmount);
            categoryMap.put(entry.getKey(), subcategoryMap);
        }
        
        // Create category chart
        JFreeChart chart = ChartFactory.createPieChart("Spending Categories", dataset, true, true, false);
        chart.setBackgroundPaint(NORMALTILECOLOR);
        chart.getTitle().setFont(new java.awt.Font("Segoe UI", 0, 18));
        chart.addSubtitle(new TextTitle(subtitleString));
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelBackgroundPaint(Color.WHITE);
        ChartPanel chartPanel = new ChartPanel(chart);
        StandardPieToolTipGenerator stip = new StandardPieToolTipGenerator("{0}: ${1}", new DecimalFormat("#,###.00"), new DecimalFormat("0.0%"));
        plot.setToolTipGenerator(stip);
        StandardPieSectionLabelGenerator slbl = new StandardPieSectionLabelGenerator(" {0} {2} ", new DecimalFormat("#,##0"), new DecimalFormat("0.0%"));
        plot.setLabelGenerator(slbl);
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                // highlight subsection on hover
            }
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                // Open new pie chart window with a further categories breakdown
                ChartEntity entity = event.getEntity();
                String clickedCategory = "";
                if (entity instanceof PieSectionEntity || entity instanceof LegendItemEntity) { 
                    if (entity instanceof PieSectionEntity){
                        clickedCategory = ((PieSectionEntity) entity).getSectionKey().toString();
                    }
                    if (entity instanceof LegendItemEntity){
                        clickedCategory = ((LegendItemEntity) entity).getSeriesKey().toString();
                    }
                    DefaultPieDataset popupDataset = new DefaultPieDataset(); 
                    for (Map.Entry<String, Map<String, Double>> category : categoryMap.entrySet()) {
                        if (clickedCategory.equals(category.getKey())) {
                            for (Map.Entry<String, Double> subcategory : category.getValue().entrySet()) {
                                popupDataset.setValue(subcategory.getKey(), subcategory.getValue());
                            }
                        }
                    }
                    
                    // Create new chart popup
                    JFreeChart popupChart = ChartFactory.createPieChart("Category Breakdown", popupDataset, true, true, false);
                    popupChart.setBackgroundPaint(NORMALTILECOLOR);
                    popupChart.getTitle().setFont(new java.awt.Font("Segoe UI", 0, 18));
                    PiePlot popupPlot = (PiePlot) popupChart.getPlot();
                    popupPlot.setLabelBackgroundPaint(Color.WHITE);
                    ChartPanel popupChartPanel = new ChartPanel(popupChart);
                    StandardPieToolTipGenerator stipPopup = new StandardPieToolTipGenerator("{0}: ${1}", new DecimalFormat("#,###.00"), new DecimalFormat("0.0%"));
                    popupPlot.setToolTipGenerator(stipPopup);
                    StandardPieSectionLabelGenerator slblPopup = new StandardPieSectionLabelGenerator(" {0} {2} ", new DecimalFormat("#,##0"), new DecimalFormat("0.0%"));
                    popupPlot.setLabelGenerator(slblPopup);
                    ChartFrame frame = new ChartFrame("Category Breakdown", popupChart);
                    frame.setSize(600, 450); 
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true); 
                }
            }
        });
        // Remove old chart and add new chart
        Component[] components = spendingPieChart.getComponents();
        if (components.length != 0) {
            for (Component component : components) {
                spendingPieChart.remove(component);
            }
        }
        spendingPieChart.setPreferredSize(new Dimension(485, 340));
        spendingPieChart.add(chartPanel);
        spendingPieChart.revalidate();
        spendingPieChart.repaint();        
    }
    
    private void stockPanelPopulation() throws InterruptedException, ExecutionException, IOException, ParseException {
        ApiFuture<QuerySnapshot> future = db.collection("users").document(QuickstartApplication.userID).collection("stocks").get();
        List<QueryDocumentSnapshot> stocks = new ArrayList <>();
        stocks = future.get().getDocuments();
        stockIteratorXAxis = 0;
        stockIteratorYAxis = 0;
        JPanel finishedPanel = new javax.swing.JPanel();
        finishedPanel.setLayout(new java.awt.GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
             
        for (QueryDocumentSnapshot  stock : stocks) {
            JPanel panel = new javax.swing.JPanel();
            panel.setBackground(NORMALTILECOLOR);
            panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.PAGE_AXIS));
            panel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            panel.setFocusable(true);
            panel.add(Box.createRigidArea(new Dimension(10,5)));
            
            String[] symbols = new String[] {stock.getId()};
            // Can also be done with explicit from, to and Interval parameters
            Map<String, Stock> stocksGrab = YahooFinance.get(symbols, true);
            Stock stockInfo = stocksGrab.get(stock.getId());
            if (stockInfo == null) {
                break;
            }
            BigDecimal currentStockPrice = stockInfo.getQuote().getPrice();
            JLabel ticker = new javax.swing.JLabel(stock.getId() + " $" + df.format(currentStockPrice));
            ticker.setFont(new java.awt.Font("Segoe UI", 0, 24));
            panel.add(ticker);
            panel.add(Box.createRigidArea(new Dimension(10,10)));
            String sharesAmount = stock.get("shares").toString();
            JLabel shares = new javax.swing.JLabel(sharesAmount + " shares owned");
            shares.setFont(new java.awt.Font("Segoe UI", 0, 14));
            panel.add(shares);
            String costPerShare = stock.get("cost").toString();
            JLabel cost = new javax.swing.JLabel("$" + costPerShare + "/share");
            cost.setFont(new java.awt.Font("Segoe UI", 0, 14));
            panel.add(cost);     
            panel.add(Box.createRigidArea(new Dimension(10,10)));
            Double currentAmount = Double.valueOf(df.format(currentStockPrice)) * Double.valueOf(sharesAmount);
            JLabel currentTotal = new javax.swing.JLabel("Market value: $" + df.format(currentAmount));
            currentTotal.setFont(new java.awt.Font("Segoe UI", 0, 14));
            panel.add(currentTotal);    
            Double costAmount = Double.valueOf(costPerShare) * Double.valueOf(sharesAmount);
            Double profitLossAmount = currentAmount - costAmount;
            JLabel profitLossTotal = new javax.swing.JLabel("Open P/L: $" + df.format(profitLossAmount));
            profitLossTotal.setFont(new java.awt.Font("Segoe UI", 0, 14));
            if (profitLossAmount > 0) {
                profitLossTotal.setForeground(new Color(60,179,113));
            } else {
                profitLossTotal.setForeground(new Color(178,34,34));
                profitLossTotal.setText("Open P/L: $" + df.format(profitLossAmount * -1));
            }
            panel.add(profitLossTotal);             
            panel.setPreferredSize(new Dimension(180,145));
            
            // click listener to edit stock
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {  
                    addStockDialog.setVisible(true);
                    stockDialogTickerTextField.setText(stock.getId());
                    stockDialogTickerTextField.setEditable(false);
                    stockDialogTickerTextField.setEnabled(false);
                    stockDialogSharesTextField.setText(sharesAmount);
                    stockDialogCostTextField.setText(costPerShare);
                    stockDialogDateTextField.setText(stock.get("date").toString());
                    stockDialogAddOrEditButton.setText("Edit");
                    stockDialogDeleteButton.setVisible(true);
                }
            });
            tileClickColorChange(panel);

            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(15, 15, 0, 0);
            c.weightx = 0.001;
            c.weighty = 0.001;   
            c.insets = new Insets(15, 15, 0, 0);
            c.gridx = stockIteratorXAxis;
            c.gridy = stockIteratorYAxis;
            stockIteratorXAxis++;               
            if (stockIteratorXAxis == 3) {   // move to next row
                c.insets = new Insets(15, 15, 0, 30);
                stockIteratorXAxis = 0;
                stockIteratorYAxis++;
            }          
            finishedPanel.add(panel, c);
            
            
            // Graph setup -- API calls are way too slow. can not include for now.
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            cal.setTime(sdf.parse(stock.get("date").toString()));// all done
            List<HistoricalQuote> fullHistory = stockInfo.getHistory(cal, Interval.WEEKLY);
            
            
            final TimeSeries s1 = new TimeSeries(stock.getId());
            for (HistoricalQuote quote : fullHistory){
                Calendar calendar = quote.getDate();
                ZoneId zoneId = ZoneId.systemDefault();
                Date date = calendar.getTime();
                LocalDate ld = date.toInstant().atZone(zoneId).toLocalDate();
                Day day = new Day(ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
                s1.add(day, Double.valueOf(quote.getClose().toString()));
            }
            dataset.addSeries(s1);
            
        }
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Stock Performance Over Time", null, "Amount (in USD)", dataset, true, true, false);
        chart.getTitle().setFont(new java.awt.Font("Segoe UI", 0, 18));
        chart.setBackgroundPaint(NORMALTILECOLOR);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYToolTipGenerator xyttg = new StandardXYToolTipGenerator(" {1}: {2} ", DateFormat.getDateInstance(), NumberFormat.getCurrencyInstance());
        plot.getRenderer().setDefaultToolTipGenerator(xyttg);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(true);
        ChartPanel chartPanel = new ChartPanel(chart); 
        
        Component[] components = jPanel2.getComponents();
        if (components.length != 0) {
            for (Component component : components) {
                jPanel2.remove(component);
            }
        }
        
        jPanel2.setPreferredSize(new Dimension(474, 306));
        jPanel2.add(chartPanel);
        jPanel2.revalidate();
        jPanel2.repaint();
        investmentsTab.revalidate();
        investmentsTab.repaint(); 
        
        
        // Add blank filler panels for formatting if only 1 or 2 stock panels exist
        if (stockIteratorXAxis == 1 || stockIteratorXAxis == 2) {
            if (stockIteratorXAxis == 1) {
                JPanel fillerTile1 = new javax.swing.JPanel();
                fillerTile1.add(Box.createRigidArea(new Dimension(10,10)));
                fillerTile1.setBorder(null);
                c.anchor = GridBagConstraints.NORTHWEST;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.insets = new Insets(15, 15, 0, 0);
                c.weightx = 0.001;
                c.weighty = 0.001;   
                c.insets = new Insets(15, 15, 0, 0);
                c.gridx = stockIteratorXAxis;
                c.gridy = stockIteratorYAxis;
                stockIteratorXAxis++;  
                finishedPanel.add(fillerTile1, c);
            } 
            if (stockIteratorXAxis == 2) {
                JPanel fillerTile2 = new javax.swing.JPanel();
                fillerTile2.add(Box.createRigidArea(new Dimension(10,10)));
                fillerTile2.setBorder(null);
                c.anchor = GridBagConstraints.NORTHWEST;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.insets = new Insets(15, 15, 0, 0);
                c.weightx = 0.001;
                c.weighty = 0.001;   
                c.insets = new Insets(15, 15, 0, 0);
                c.gridx = stockIteratorXAxis;
                c.gridy = stockIteratorYAxis;
                stockIteratorXAxis++;  
                finishedPanel.add(fillerTile2, c);
            }
        }
        investmentsDisplayStocksScrollPane.setViewportView(finishedPanel);
    } 
    
    /**
     * @throws java.lang.InterruptedException
     */
    public static void main() throws InterruptedException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Client().setVisible(true);
                } catch (InterruptedException | ParseException | ExecutionException | IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel accountDetailsPopup;
    private javax.swing.JPanel accountPanel;
    private javax.swing.JPanel accountPopupTab;
    private javax.swing.JScrollPane accountScrollPanel;
    private javax.swing.JPanel accountTab;
    private javax.swing.JLabel accountableHeaderLabel;
    private javax.swing.JLayeredPane accountableHeaderPanel;
    private javax.swing.JLabel actualAllocationLabel;
    private javax.swing.JLabel actualAmountAllocationLabel;
    private javax.swing.JFormattedTextField addGoalAmountSavedField;
    private javax.swing.JLabel addGoalAmountSavedLabel;
    private javax.swing.JFormattedTextField addGoalBeginDateField;
    private javax.swing.JLabel addGoalBeginDateLabel;
    private javax.swing.JButton addGoalButton;
    private javax.swing.JPanel addGoalContainer;
    private javax.swing.JLabel addGoalDateFormatLabel1;
    private javax.swing.JLabel addGoalDateFormatLabel2;
    private javax.swing.JDialog addGoalDialog;
    private javax.swing.JFormattedTextField addGoalEndDateField;
    private javax.swing.JLabel addGoalEndDateLabel;
    private javax.swing.JFormattedTextField addGoalGoalAmountField;
    private javax.swing.JLabel addGoalGoalAmountLabel;
    private javax.swing.JFormattedTextField addGoalItemDescField;
    private javax.swing.JLabel addGoalItemDescLabel;
    private javax.swing.JPanel addGoalPanel;
    private javax.swing.JDialog addStockDialog;
    private javax.swing.JPanel addStockPanel;
    private javax.swing.JLabel availableAllocationLabel;
    private javax.swing.JLabel availableAmountAllocationLabel;
    private javax.swing.JLabel availableCreditLabel;
    private javax.swing.JPanel availableCreditPanel;
    private javax.swing.JLabel balanceLabel;
    private javax.swing.JPanel balanceTile;
    private javax.swing.JPanel cards;
    private javax.swing.JLayeredPane closeAppPanel;
    private javax.swing.JLabel closeLabel;
    private javax.swing.JPanel debtsPanel;
    private javax.swing.JScrollPane debtsScrollPanel;
    private javax.swing.JPanel debtsTab;
    private javax.swing.JLabel dueDateLabel;
    private javax.swing.JPanel dueDatePanel;
    private javax.swing.JPanel goalsAllocationPanel;
    private javax.swing.JPanel goalsInfoPanel;
    private javax.swing.JLabel goalsLabelPanel;
    private javax.swing.JPanel goalsPanel;
    private javax.swing.JScrollPane goalsScrollPane;
    private javax.swing.JTable goalsTable;
    private javax.swing.JScrollPane goalsTableScrollPane;
    private javax.swing.JPanel goalsTitlePanel;
    private javax.swing.JPanel homeTab;
    private javax.swing.JPanel homeTilesPanel;
    private javax.swing.JPanel incomeAndExpenseGraph;
    private javax.swing.JButton initiateGoalDialogButton;
    private javax.swing.JPanel investmentsAccountPanel;
    private javax.swing.JScrollPane investmentsAccountScrollPanel;
    private javax.swing.JScrollPane investmentsDisplayStocksScrollPane;
    private javax.swing.JLabel investmentsLabel;
    private javax.swing.JButton investmentsPanelAddStock;
    private javax.swing.JLabel investmentsPanelStockLabel;
    private javax.swing.JPanel investmentsStockPanel;
    private javax.swing.JPanel investmentsTab;
    private javax.swing.JPanel investmentsTile;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton linkButton;
    private javax.swing.JPanel listedTransactionsPanel;
    private javax.swing.JLabel loansLabel;
    private javax.swing.JPanel loansTile;
    private static javax.swing.JTabbedPane masterTabPane;
    private javax.swing.JLabel minPaymentLabel;
    private javax.swing.JPanel minimumPaymentPanel;
    private javax.swing.JPanel mortgageCard;
    private javax.swing.JLabel mortgageLabel1;
    private javax.swing.JLabel mortgageLabel2;
    private javax.swing.JLabel mortgageLabel3;
    private javax.swing.JLabel mortgageLabel4;
    private javax.swing.JLabel mortgageLabel5;
    private javax.swing.JLabel mortgageLabel6;
    private javax.swing.JLabel mortgageLabel7;
    private javax.swing.JLabel mortgageLabel8;
    private javax.swing.JLabel mortgageLabel9;
    private javax.swing.JPanel mortgageLayoutPanel;
    private javax.swing.JButton newAccountContinueButton;
    private javax.swing.JDialog newAccountDialog;
    private javax.swing.JPanel newAccountPanel;
    private javax.swing.JScrollPane newAccountScrollPane;
    private javax.swing.JTextArea newAccountTextField;
    private javax.swing.JButton payBillButton;
    private javax.swing.JLabel popupAccountMaskedNumber;
    private javax.swing.JLabel popupAccountName;
    private javax.swing.JLabel popupAccountType;
    private javax.swing.JLabel popupAvailableCredit;
    private javax.swing.JLabel popupBalance;
    private javax.swing.JLabel popupDueDate;
    private javax.swing.JLabel popupMinPayment;
    private javax.swing.JLabel popupSeperatorLabel;
    private javax.swing.JButton profileReloadButton;
    private javax.swing.JLabel recentTransactionsLabel;
    private javax.swing.JButton saveGoalEditsButton;
    private javax.swing.JPanel savingsGraph;
    private javax.swing.JLabel savingsLabel;
    private javax.swing.JPanel savingsPanel;
    private javax.swing.JLabel savingsQuickHistoryLabel;
    private javax.swing.JScrollPane savingsQuickHistoryScrollPane;
    private javax.swing.JTable savingsQuickHistoryTable;
    private javax.swing.JScrollPane savingsScrollPane;
    private javax.swing.JPanel savingsTab;
    private javax.swing.JPanel savingsTile;
    private javax.swing.JPanel spendingGraph;
    private javax.swing.JLabel spendingLabel;
    private javax.swing.JPanel spendingPieChart;
    private javax.swing.JScrollPane spendingScrollPane;
    private javax.swing.JPanel spendingTab;
    private javax.swing.JTable spendingTable;
    private javax.swing.JButton stockDialogAddOrEditButton;
    private javax.swing.JLabel stockDialogCostLabel;
    private javax.swing.JTextField stockDialogCostTextField;
    private javax.swing.JLabel stockDialogDateFormatLabel;
    private javax.swing.JLabel stockDialogDateLabel;
    private javax.swing.JTextField stockDialogDateTextField;
    private javax.swing.JButton stockDialogDeleteButton;
    private javax.swing.JLabel stockDialogSharesLabel;
    private javax.swing.JTextField stockDialogSharesTextField;
    private javax.swing.JLabel stockDialogTickerLabel;
    private javax.swing.JTextField stockDialogTickerTextField;
    private javax.swing.JPanel studentCard;
    private javax.swing.JLabel studentLabel1;
    private javax.swing.JLabel studentLabel10;
    private javax.swing.JLabel studentLabel11;
    private javax.swing.JLabel studentLabel12;
    private javax.swing.JLabel studentLabel2;
    private javax.swing.JLabel studentLabel3;
    private javax.swing.JLabel studentLabel4;
    private javax.swing.JLabel studentLabel5;
    private javax.swing.JLabel studentLabel6;
    private javax.swing.JLabel studentLabel7;
    private javax.swing.JLabel studentLabel8;
    private javax.swing.JLabel studentLabel9;
    private javax.swing.JPanel studentLayoutPanel;
    private javax.swing.JPanel tableCard;
    private javax.swing.JLabel totalBalance;
    private javax.swing.JLabel totalInvestments;
    private javax.swing.JLabel totalLoans;
    private javax.swing.JLabel totalSavings;
    private javax.swing.JLabel transactionsLabel;
    private javax.swing.JPanel transactionsPanel;
    private javax.swing.JScrollPane transactionsScrollPane;
    private javax.swing.JScrollPane transactionsScrollPopup;
    private javax.swing.JTable transactionsTable;
    // End of variables declaration//GEN-END:variables
}
