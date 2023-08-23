package com.example.pos1;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CATEGORY_MAST = "category_mast";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
//        String createUserTableQuery = "CREATE TABLE users (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "name TEXT," +
//                "email TEXT)";
//        db.execSQL(createUserTableQuery);

        db.execSQL("CREATE TABLE IF NOT EXISTS users (user_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, phone TEXT, password TEXT, logo TEXT, is_login INTEGER, is_manager INTEGER, last_login TEXT, device_id TEXT, server_check INTEGER DEFAULT 0, created_at TEXT, updated_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS zone_mast (zone_id INTEGER PRIMARY KEY AUTOINCREMENT, zone_name VARCHAR (50), status BOOLEAN DEFAULT (1), server_check INTEGER DEFAULT (0), price DOUBLE (10, 2) DEFAULT (0))");

        db.execSQL("CREATE TABLE category_mast (catid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "catname TEXT UNIQUE, uentdt TEXT," +
                "is_active INT,description TEXT," +
                "is_deal INT DEFAULT 0,order_no TEXT," +
                "sztxt TEXT,bstxt	TEXT,cotxt TEXT,extxt TEXT," +
                "anytxt TEXT,nbtxt TEXT,slug TEXT," +
                "catnmtxt TEXT,photo TEXT, is_maintain_stock INT DEFAULT 0," +
                "server_check INTEGER DEFAULT 0, allow_stock_qty INT DEFAULT 0," +
                " is_combo BOOLEAN DEFAULT (0), web_catid INT DEFAULT 0," +
                " contains TEXT, extras TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS suburbs (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, post_code TEXT, zone INTEGER, server_check INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE IF NOT EXISTS size_mast (smzid INTEGER PRIMARY KEY AUTOINCREMENT, szname TEXT, cat_id INT, is_active INT, server_check INTEGER DEFAULT 0, uentdt TEXT, szid INT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS size_mast_data (szid INTEGER PRIMARY KEY AUTOINCREMENT, szname TEXT, is_active INT, server_check INTEGER DEFAULT 0, uentdt TEXT, system_defined BOOLEAN DEFAULT (0))");

//        db.execSQL("CREATE TABLE IF NOT EXISTS sqlite_sequence(name,seq)");

        db.execSQL("CREATE TABLE IF NOT EXISTS base_mast (bid INTEGER PRIMARY KEY AUTOINCREMENT,bname TEXT,is_active INT DEFAULT 1,server_check INTEGER DEFAULT 0, uentdt TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS size_base_data (sbid INTEGER PRIMARY KEY AUTOINCREMENT,catid INT,szid INT, bid INT, server_check INTEGER DEFAULT 0, uent_dt TEXT)");

        db.execSQL("CREATE TABLE item_master (tid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cat_id TEXT,code TEXT, name TEXT,status INT DEFAULT 1," +
                "pickup_price TEXT,delivery_price TEXT,eat_in_price TEXT,cost_price TEXT," +
                "stock TEXT,min_stock TEXT,item_img TEXT,bar_code TEXT,descc TEXT," +
                "contain TEXT,extra TEXT,prices TEXT, created_at TEXT, server_check INTEGER DEFAULT 0," +
                " deactive_dt TEXT,is_combo BOOLEAN DEFAULT (0)," +
                "itmtxt VARCHAR (120), btn_bg_color VARCHAR (10), btn_font_color VARCHAR (10)," +
                " is_deleted INTEGER DEFAULT (0))");


        db.execSQL("CREATE TABLE IF NOT EXISTS item_any (id INTEGER PRIMARY KEY AUTOINCREMENT,itemid INT,name TEXT, how_many INT, order_num INT, is_must INT DEFAULT 0, topp_idz TEXT, server_check INTEGER DEFAULT 0, created_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS topping_mast (tid INTEGER PRIMARY KEY AUTOINCREMENT, tname TEXT, is_active INT, server_check INTEGER DEFAULT 0, uentdt TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS topping_info (id INTEGER PRIMARY KEY AUTOINCREMENT, topp_id INT, szid INT, p_plus TEXT, p_double TEXT, p_h_plus TEXT, p_h_double TEXT, server_check INTEGER DEFAULT 0, created_at TEXT, catid INT DEFAULT 0)");

        db.execSQL("CREATE TABLE IF NOT EXISTS happy_hours (id INTEGER PRIMARY KEY AUTOINCREMENT, tid INTEGER, distype CHAR (1), disval DOUBLE (8, 2), date_from DATE, date_to DATE, time_from TIME, time_to TIME, created_at TEXT, server_check INTEGER DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS holiday_settings (hid INTEGER PRIMARY KEY AUTOINCREMENT, htitle VARCHAR (200), hmsg TEXT, is_active BOOLEAN DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS order_cancel_settings (ocid INTEGER PRIMARY KEY AUTOINCREMENT, is_active BOOLEAN DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS order_complete_time (tmid INTEGER PRIMARY KEY AUTOINCREMENT, walkintm INT (4) DEFAULT (0), order_take_time TIME)");

        db.execSQL("CREATE TABLE IF NOT EXISTS server (id INTEGER PRIMARY KEY AUTOINCREMENT, ip TEXT, is_parent INTEGER, multisystem INTEGER, server_check INTEGER DEFAULT 0, ref_id TEXT, updated_at TEXT, domain VARCHAR (150), webtoken VARCHAR (150), is_skip BOOLEAN DEFAULT(0), is_upload_item_csv BOOLEAN DEFAULT (0), is_upload_topping_csv BOOLEAN DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS login_history (id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,device_id TEXT,actions INTEGER DEFAULT 0,server_check INTEGER DEFAULT 0, created_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY AUTOINCREMENT,web_id INTEGER,WebRef TEXT,CustName TEXT,CustPhone TEXT,CustAddress TEXT,CustZip TEXT,CustEmail TEXT,CustNotes TEXT,WebOrderType TEXT,WebDelFee TEXT,WebDiscount TEXT,WebTotalAmt TEXT,WebPaidAmt TEXT,WebGST TEXT,WebOrderValue TEXT,WebOrderDate TEXT,WebTime TEXT,WebPrepTime TEXT,WebDueDate TEXT,WebDueTime TEXT,Processed TEXT,WebOnlFee TEXT,items TEXT,server_check INTEGER DEFAULT 0, created_at TEXT, WebOrderAsap CHAR (1), PayMode VARCHAR (2), PayStatus VARCHAR (20))");

        db.execSQL("CREATE TABLE IF NOT EXISTS printers (id INTEGER PRIMARY KEY AUTOINCREMENT,category_id INTEGER,item_id INTEGER,printer_name TEXT,copy INTEGER DEFAULT 1,status INTEGER DEFAULT 0,server_check INTEGER DEFAULT 0, created_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS printer_copy (id INTEGER PRIMARY KEY AUTOINCREMENT,p_name TEXT,copy INTEGER,server_check INTEGER DEFAULT 0, updated_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS customers (id INTEGER PRIMARY KEY AUTOINCREMENT, phone INTEGER, name TEXT, email TEXT, loyalty_num TEXT, suburb TEXT, addr1 TEXT, addr2 TEXT, state TEXT, pin_code TEXT, notes TEXT, order_type VARCHAR (20), order_status VARCHAR (20), server_check INTEGER DEFAULT 0, created_at TEXT, web_id INTEGER DEFAULT (0), edate DATE, taken_time TIME, due_time TIME, order_completed BOOLEAN DEFAULT (0), order_cancelled BOOLEAN DEFAULT (0), send_to_kitchen BOOLEAN DEFAULT (0), driver_staff_id INTEGER DEFAULT (0), zone_name VARCHAR (50), delcost DOUBLE (10, 2), order_cancelled_status VARCHAR (20), order_cancelled_reason VARCHAR (150))");

        db.execSQL("CREATE TABLE IF NOT EXISTS rooms (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,status INTEGER DEFAULT 1, server_check INTEGER DEFAULT 0,created_at TEXT, updated_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS tables (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,room_id INTEGER,chairs INTEGER,status INTEGER DEFAULT 1, server_check INTEGER DEFAULT 0,created_at TEXT, updated_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS table_bookings (id INTEGER PRIMARY KEY AUTOINCREMENT,rvid INTEGER DEFAULT 0,date TEXT,in_time TEXT,out_time TEXT,guest_num INTEGER,room_id INTEGER,table_nums TEXT, merge INTEGER DEFAULT 0,deposit_taken INTEGER DEFAULT 0,function_type TEXT,deposit_amount TEXT,notes TEXT,phone TEXT,cust_name TEXT,email TEXT,address TEXT,suburb TEXT,post_code TEXT,status INTEGER DEFAULT 1,created_at TEXT, updated_at TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS temp_book_req (id INTEGER PRIMARY KEY AUTOINCREMENT, info TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS button_settings (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,slug TEXT,icon TEXT,status INTEGER DEFAULT 1, server_check INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE IF NOT EXISTS setting_default (id INTEGER PRIMARY KEY AUTOINCREMENT, printer TEXT, copy INTEGER DEFAULT 1, printer_font INTEGER (3) DEFAULT (0), reciept_print_settings TEXT, surcharge_per DECIMAL (3,2), surcharge_days VARCHAR (15), surcharge_dtype VARCHAR (10))");

        db.execSQL("CREATE TABLE IF NOT EXISTS combo_item_data (combo_item_id INTEGER PRIMARY KEY AUTOINCREMENT,combo_id INT (4),catid INT (4),item_base_price DOUBLE (10, 2),order_no INT (4),szid INT (4),items TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS combo_mast (combo_id INTEGER PRIMARY KEY AUTOINCREMENT,catid INT (4),combo_name VARCHAR (100),totcost DOUBLE (10, 2),discount_type CHAR (1),discount_amt DOUBLE (10, 2),combocost DOUBLE (10, 2),is_active BOOLEAN DEFAULT (0),uent_dt DATETIME)");

        db.execSQL("CREATE TABLE IF NOT EXISTS company_mast (compid INTEGER PRIMARY KEY AUTOINCREMENT,company_name VARCHAR (120),address VARCHAR (250),contact_no VARCHAR (15),abn_no VARCHAR (30))");

        db.execSQL("CREATE TABLE IF NOT EXISTS order_data (order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "odate DATE," +
                "custid BIGINT," +
                "ordertype VARCHAR (20)," +
                "ordercost DOUBLE (12, 2)," +
                "order_status VARCHAR (10)," +
                "webfree DOUBLE (12, 2)," +
                "gst DOUBLE (12, 2)," +
                "delcost DOUBLE (12, 2)," +
                "discount DOUBLE (12, 2)," +
                "cnote TEXT," +
                "suburb VARCHAR (30)," +
                "paymode VARCHAR (10)," +
                "txn_id VARCHAR (120)," +
                "payment_status VARCHAR (120)," +
                "payment_type VARCHAR (120)," +
                "payer_status VARCHAR (120)," +
                "payer_email VARCHAR (120)," +
                "redeem_amt DOUBLE (12, 2)," +
                "coupon_code VARCHAR (15)," +
                "dvdate DATE," +
                "uent_dt DATETIME," +
                "cashamt DOUBLE (12, 2)," +
                "eftpos DOUBLE (12, 2)," +
                "server_type VARCHAR (10)," +
                "refund_amt DOUBLE (12, 2)," +
                "refund_type VARCHAR (10)," +
                "refund_note VARCHAR (200)," +
                "refund_date DATE," +
                "edate DATE," +
                "onaccount BOOLEAN DEFAULT (0)," +
                "surcharge_cost DOUBLE (10, 2) DEFAULT (0), paycost DOUBLE (10, 2) DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS order_product_data (pro_id INTEGER PRIMARY KEY AUTOINCREMENT, order_id INTEGER, custid INTEGER, catid INTEGER, catname VARCHAR (100), itemid VARCHAR (30), itemname VARCHAR (150), sitemid VARCHAR (30), sitemname VARCHAR (150), qty DOUBLE (8, 2), size VARCHAR (20), base VARCHAR (20), price DOUBLE (10, 2), fhprice DOUBLE (10, 2), shprice DOUBLE (10, 2), price_with_topping DOUBLE (10, 2), totprice DOUBLE (10, 2), hnh BOOLEAN DEFAULT (0), is_own BOOLEAN DEFAULT (0), is_deal BOOLEAN DEFAULT (0), parent_pro_id INTEGER, contains TEXT, extras TEXT, fritems TEXT, sfritems TEXT, anyitems TEXT, remark TEXT, itemprice DOUBLE (10, 2), extraprice DOUBLE (10, 2), refund_amt DOUBLE (12, 2), refund_type VARCHAR (10), refund_note VARCHAR (200), refund_date DATE, uent_dt DATETIME, is_amt_paid BOOLEAN DEFAULT (0), is_item_print BOOLEAN DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS state_mast (state_id INTEGER PRIMARY KEY AUTOINCREMENT,state_name VARCHAR (50))");

        db.execSQL("CREATE TABLE IF NOT EXISTS tmp_order_product_data (tmpid INTEGER PRIMARY KEY AUTOINCREMENT, custid INTEGER, catid INTEGER, catname VARCHAR (100), itemid VARCHAR (30), itemname VARCHAR (150), sitemid VARCHAR (30), sitemname VARCHAR (150), qty DOUBLE (8, 2), size VARCHAR (20), base VARCHAR (20), price DOUBLE (10, 2), fhprice DOUBLE (10, 2), shprice DOUBLE (10, 2), price_with_topping DOUBLE (10, 2), totprice DOUBLE (10, 2), hnh BOOLEAN DEFAULT (0), is_own BOOLEAN DEFAULT (0), is_deal BOOLEAN DEFAULT (0), parent_pro_id INTEGER, contains TEXT, extras TEXT, fritems TEXT, sfritems TEXT, anyitems TEXT, remark TEXT, itemprice DOUBLE (10, 2), extraprice DOUBLE (10, 2), uent_dt DATETIME, is_item_print BOOLEAN DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS print_group_mast (pgid INTEGER PRIMARY KEY AUTOINCREMENT,group_name VARCHAR (50))");

        db.execSQL("CREATE TABLE IF NOT EXISTS category_group_data (cgid INTEGER PRIMARY KEY AUTOINCREMENT, cgtxt TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS security_settings_mast (ssid INTEGER PRIMARY KEY AUTOINCREMENT, ssname VARCHAR (150))");

        db.execSQL("CREATE TABLE IF NOT EXISTS staff_mast (stid INTEGER PRIMARY KEY AUTOINCREMENT, uname VARCHAR (20), uemail VARCHAR (120), uphno VARCHAR (10), delrate DOUBLE (10, 2), hrrate DOUBLE (10, 2), sid INTEGER, stdt DATE, pwd VARCHAR (100))");

        db.execSQL("CREATE TABLE IF NOT EXISTS user_security_data (usid INTEGER PRIMARY KEY AUTOINCREMENT, sid INTEGER, ssid INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS user_security_mast (sid INTEGER PRIMARY KEY AUTOINCREMENT, profile_name VARCHAR (30), order_no INTEGER, is_delete BOOLEAN DEFAULT (0), system_defined BOOLEAN DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS category_group_data (cgid INTEGER PRIMARY KEY AUTOINCREMENT, cgtxt TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS otype_mast (otid INTEGER PRIMARY KEY AUTOINCREMENT, otname VARCHAR (20), is_active BOOLEAN DEFAULT (1), server_check INTEGER DEFAULT (0))");

        db.execSQL("CREATE TABLE IF NOT EXISTS del_pick_time (tmid INTEGER PRIMARY KEY AUTOINCREMENT, deltm INT (4) DEFAULT (0), picktm INT (4) DEFAULT (0))");

        /**************Insert data to table******************/
        String current_dt = getCurrentDateTime();
//        var receipt_json_data = '[{"reciept_name":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_email":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_phone":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_address":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_ordno":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_ordtype":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_orddt":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_duedt":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_item":{"fontsize":"16px","fontbold":"N","show":"Y"}},{"reciept_note":{"fontsize":"16px","fontbold":"N","show":"Y"}}]';

//        String sql = "INSERT INTO users (name, email, password, is_login, is_manager, created_at) VALUES (?, ?, ?, ?, ?, ?)";
//        SQLiteStatement statement = db.compileStatement(sql);
//
//// Bind the values to the statement
//        statement.bindString(1, "super");
//        statement.bindString(2, "super@esofttechnologies.com.au");
//        statement.bindString(3, "esoft@123");
//        statement.bindLong(4, 0);
//        statement.bindLong(5, 0);
//        statement.bindString(6, current_dt);

//        statement.executeInsert();
//        db.execSQL("INSERT INTO category_mast (catname, uentdt, is_active, description) VALUES ('Category 1', '" + current_dt + "', 1, 'Sample description 1')");

        db.execSQL("INSERT INTO size_mast (szname, cat_id, is_active, uentdt) VALUES ('NS', 1, 1, '" + current_dt + "')");

        db.execSQL("INSERT INTO base_mast (bname, is_active, uentdt) VALUES ('NB', 1, '" + current_dt + "')");

        db.execSQL("INSERT INTO server (ip, is_parent, multisystem, updated_at, domain, webtoken, is_skip) VALUES ('127.0.0.1', 1, 1, '" + current_dt + "', '', '', 0)");

        db.execSQL("INSERT INTO temp_book_req (info) VALUES ('')");

        db.execSQL("INSERT INTO button_settings (name, slug, icon) VALUES ('', 'eatin', 'fa fa-utensil-spoon'), ('', 'pickup', 'fas fa-shopping-bag'), ('', 'delivery', 'fas fa-truck'), ('', 'counter', 'fas fa-walking'), ('', 'quick_sale', 'fa fa-percent'), ('', 'change_status', 'fa fa-asterisk'), ('', 'print_dash', 'fa fa-print'), ('', 'active_ord', ''), ('', 'all_ord', ''), ('', 'sr_pickup', ''), ('', 'sr_delivery', ''), ('', 'sr_counter', ''), ('', 'sr_eatin', ''), ('', 'web_ord', ''), ('', 'sr_completed', '')");

//        db.execSQL("INSERT INTO setting_default (printer, copy, printer_font, reciept_print_settings, surcharge_per, surcharge_days, surcharge_dtype) VALUES ('', 1, 0, '"+ receipt_json_data +"', 0, '', '')");

        db.execSQL("INSERT INTO state_mast (state_name) VALUES ('NSW'), ('QLD'), ('SA'), ('TAS'), ('VIC'), ('WA')");

        db.execSQL("INSERT INTO company_mast (company_name, address, contact_no, abn_no) VALUES ('', '', '', '')");

        db.execSQL("INSERT INTO security_settings_mast (ssname, ssid) VALUES ('Allow Security On All Button', 1), ('Allow To Delete Items', 2), ('Allow Security On Clocking Reports', 3), ('Allow Security On Clocking', 4), ('Allow Security On Clocking Roster', 5), ('Allow Security On Sales Summary', 6), ('Allow Security On Manger Button', 7), ('Allow Security On Daily Reports', 9), ('Allow Security On Reports', 10), ('Prices On Edit Form', 11), ('Allow To Change Price On DEALS', 12), ('Allow To Cancel An Order', 13), ('Allow To Apply Discount', 14), ('Allow Security On Staff Editing', 15), ('Allow Security On Settings Button', 16), ('Allow Security On Security Profile Button', 17), ('Allow To View Reports', 18), ('Allow To Refund An Order', 19), ('Allow To Enter Items/Update', 20), ('UnPay A Paid Order', 21), ('UnCancel A Cancelled Order', 22), ('Open Cash Drawer', 23), ('Modify Order After Completed', 24), ('Delete Items From Items Inventory', 25), ('Change Price On Edit Form', 26), ('Not Allow Membership Info', 27)");

        db.execSQL("INSERT INTO otype_mast (otid, otname, is_active, server_check) VALUES (1, 'Del', 1, 0), (2, 'Pickup', 1, 0), (3, 'Eatin', 1, 0)");

        db.execSQL("INSERT INTO del_pick_time (deltm, picktm) VALUES (0, 0)");
        Log.d("add,,,,,,,", "onCreate: ");
//        addOdersSampleData();

        // Add 10 sample data entries
//        for (int i = 1; i <= 10; i++) {
//            ContentValues values = new ContentValues();
//            values.put("name", "User " + i);
//            values.put("email", "user" + i + "@example.com");
//            long result = db.insert("users", null, values);
//            Log.d("DatabaseHelper", "Insert result: " + result);
//        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }

    void addOdersSampleData(){
        String[] orderDates = {"2023-07-18"};
        long[] customerIds = {1};
        String[] orderTypes = {"EatIn"};
        double[] orderCosts = {50.00};
        String[] orderStatuses = {"Pay Later"};
        double[] webFrees = {5.00};
        double[] gsts = {7.50};
        double[] delCosts = {3.00};
        double[] discounts = {0.00};
        String[] cNotes = {"" };
        String[] suburbs = {"New York"};
        String[] payModes = { "Cash"};
        String[] txnIds = { "987654321"};
        String[] paymentStatuses = {"Completed"};
        String[] paymentTypes = { "Cash"};
        String[] payerStatuses = {"Verified"};
        String[] payerEmails = {"john@example.com"};
        double[] redeemAmts = {0.00};
        String[] couponCodes = {""};
        String[] dvDates = {"2023-07-19"};
        String[] uentDts = {"2023-07-18 12:00:00"};
        double[] cashAmts = {30.00};
        double[] eftposAmts = {20.00};
        String[] serverTypes = {"Regular"};
        double[] refundAmts = {0.00};
        String[] refundTypes = {""};
        String[] refundNotes = {""};
        String[] refundDates = {""};
        String[] eDates = {"2023-07-19"};
        boolean[] onAccounts = {true};
        double[] surchargeCosts = {0.00};
        double[] payCosts = {0.00};

        SQLiteDatabase db = this.getWritableDatabase();

        // Loop through the sample data arrays and insert the data into the order_data table
        for (int i = 0; i < orderDates.length; i++) {
            ContentValues values = new ContentValues();
            values.put("odate", orderDates[i]);
            values.put("custid", customerIds[i]);
            values.put("ordertype", orderTypes[i]);
            values.put("ordercost", orderCosts[i]);
            values.put("order_status", orderStatuses[i]);
            values.put("webfree", webFrees[i]);
            values.put("gst", gsts[i]);
            values.put("delcost", delCosts[i]);
            values.put("discount", discounts[i]);
            values.put("cnote", cNotes[i]);
            values.put("suburb", suburbs[i]);
            values.put("paymode", payModes[i]);
            values.put("txn_id", txnIds[i]);
            values.put("payment_status", paymentStatuses[i]);
            values.put("payment_type", paymentTypes[i]);
            values.put("payer_status", payerStatuses[i]);
            values.put("payer_email", payerEmails[i]);
            values.put("redeem_amt", redeemAmts[i]);
            values.put("coupon_code", couponCodes[i]);
            values.put("dvdate", dvDates[i]);
            values.put("uent_dt", uentDts[i]);
            values.put("cashamt", cashAmts[i]);
            values.put("eftpos", eftposAmts[i]);
            values.put("server_type", serverTypes[i]);
            values.put("refund_amt", refundAmts[i]);
            values.put("refund_type", refundTypes[i]);
            values.put("refund_note", refundNotes[i]);
            values.put("refund_date", refundDates[i]);
            values.put("edate", eDates[i]);
            values.put("onaccount", onAccounts[i]);
            values.put("surcharge_cost", surchargeCosts[i]);
            values.put("paycost", payCosts[i]);

            db.insert("order_data", null, values);
        }
        db.close();
    }
    public List<String> getData() {
        List<String> dataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));

                // Add the data to the list
                dataList.add("Name: " + name + ", Email: " + email);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        Log.d("DatabaseHelper", "DataList size: " + dataList.size()); // Log the size

        return dataList;
    }

}

