package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Inputter {
    private static Scanner sc = new Scanner(System.in);
    
    public static int getAnInteger(String inputMessage, String errorMessage){
        while(true){
            try{
                System.out.println(inputMessage); 
                int number = Integer.parseInt(sc.nextLine());
                return number;
            }catch(Exception e){
                System.out.println(errorMessage);
            }   
        }
    }

    //tạo hàm ép nhập số nguyên có giới hạn
    public static int getAnInteger(String inputMessage, String errorMessage,
                                    int lowerLimit, int upperLimit){
        while(true){
            try{
                System.out.println(inputMessage); 
                int number = Integer.parseInt(sc.nextLine());
                if(number < lowerLimit || number > upperLimit){
                    throw new Exception();
                }
                return number;
            }catch(Exception e){
                System.out.println(errorMessage);
            }   
        }
    }
    
    //tạo hàm ép nhập số nguyên duong
    public static int getAnPositiveInteger(String inputMessage, String errorMessage){
        while(true){
            try{
                System.out.println(inputMessage); 
                int number = Integer.parseInt(sc.nextLine());
                if(number <= 0){
                    throw new Exception();
                }
                return number;
            }catch(Exception e){
                System.out.println(errorMessage);
            }   
        }
    }
    
    //hàm nhập chuỗi, đc phép bỏ trống
    public static String getAString(String inputMessage){
        try{
            System.out.println(inputMessage); 
            String str = sc.nextLine();
            if(str.isEmpty()){
                throw new Exception();
            }
            return str.trim();
        }catch(Exception e){
            return null;
        }
    }
    
    //hàm ép nhập chuỗi, khộng đc bỏ trống
    public static String getAString(String inputMessage, String errorMessage){
        while(true){
            try{
                System.out.println(inputMessage); 
                String str = sc.nextLine();
                if(str.isEmpty()){
                    throw new Exception();
                }
                return str.trim();
            }catch(Exception e){
                System.out.println(errorMessage);
            }   
        }
    }
    
    //hàm ép nhập chuỗi phải đúng regex
    public static String getAString(String inputMessage, String errorMessage,
                                    String regex){
        while(true){
            try{
                System.out.println(inputMessage); 
                String str = sc.nextLine();
                if(str.isEmpty() || !str.matches(regex)){
                    throw new Exception();
                }
                return str.trim();
            }catch(Exception e){
                System.out.println(errorMessage);
            }
        }
    }
    
    //hàm ép nhập số thực
    public static double getADouble(String inputMessage, String errorMessage){
        while(true){
            try{
                System.out.println(inputMessage); 
                double number = Double.parseDouble(sc.nextLine());
                return number;
            }catch(Exception e){
                System.out.println(errorMessage);
            }
        }
    }
    
    //hàm ép nhap so thuc có gioi han
    public static double getADouble(String inputMessage, String errorMessage,
                                    double lowerLimit, double upperLimit){
        while(true){
            try{
                System.out.println(inputMessage); 
                double number = Double.parseDouble(sc.nextLine());
                if(number < lowerLimit || number > upperLimit){
                    throw new Exception();
                }
                return number;
            }catch(Exception e){
                System.out.println(errorMessage);
            }
        }
    }

    public static String getAString(String inputMessage, String errorMessage, int minNumber, int maxNumber){
        while(true){
            try{
                System.out.println(inputMessage); 
                String str = sc.nextLine();
                if(str.isEmpty() || (str.length() <= minNumber || str.length() >= maxNumber)) {
                    throw new Exception();
                }
                return str.trim();
            }catch(Exception e){
                System.out.println(errorMessage);
            }
        }
    }
    public static String getFullname(String inputMessage, String errorMessage, int minNumber, int maxNumber){
        while(true){
            try{
                System.out.println(inputMessage); 
                String fullname = sc.nextLine().trim();
                if(fullname.isEmpty() || !(fullname.length() >= minNumber && fullname.length() <= maxNumber)) {
                    throw new Exception();
                }
                return fullname;
            }catch(Exception e){
                System.out.println(errorMessage);
            }   
        }
    }
    
    public static String getAPhoneNumber(String inputMessage, String errorMessage){
        while(true){
            try{
                System.out.println(inputMessage); 
                String str = sc.nextLine();
                if(str.isEmpty() || (str.length() != 10) || !str.matches("^0\\d{9}$")) {
                    throw new Exception();
                }
                return str.trim();
            }catch(Exception e){
                System.out.println(errorMessage);
            }
        }
    }
    
    public static LocalDate getADate(String msg, String err){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while(true){
            try{
                System.out.print(msg);
                String s = sc.nextLine().trim();
                if(!s.matches("^\\d{2}/\\d{2}/\\d{4}$")){
                    throw new DateTimeParseException(err, "", 0);
                }
                LocalDate date = LocalDate.parse(s, df);
                if(!date.isAfter(LocalDate.now())){
                    throw new DateTimeParseException("Date must be in future!!!", "", 1);
                }
                return date;
            }catch(DateTimeParseException e){
                System.out.println(e.getMessage());
            }
        }
    }
}

