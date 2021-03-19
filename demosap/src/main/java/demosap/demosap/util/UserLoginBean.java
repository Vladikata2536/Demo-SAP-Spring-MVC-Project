package demosap.demosap.util;

public final class UserLoginBean {
public static boolean LOGGED_ADMIN = false;
public static boolean LOGGED_SELLER = false;
public static String ADMIN_NAME = null;
public static String SELLER_NAME = null;
public static boolean CURRENT_LOGGED_USER = false;

public static void loginAdmin(String adminName){
    LOGGED_ADMIN = true;
    CURRENT_LOGGED_USER = true;
    ADMIN_NAME = adminName;
}
public static void logoutAdmin(){
    LOGGED_ADMIN = false;
    ADMIN_NAME = null;
    CURRENT_LOGGED_USER = false;
}
public static void loginSeller(String sellerName){
    LOGGED_SELLER = true;
    CURRENT_LOGGED_USER = true;
    SELLER_NAME = sellerName;
}
public static void logoutSeller(){
    LOGGED_SELLER = false;
    CURRENT_LOGGED_USER = false;
    SELLER_NAME = null;
}

}
