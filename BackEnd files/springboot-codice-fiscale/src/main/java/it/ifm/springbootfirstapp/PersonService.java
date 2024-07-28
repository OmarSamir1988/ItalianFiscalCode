package it.ifm.springbootfirstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    private static final Map<String, String> ITALIAN_CITY_CODES;
    private static final Map<String, String> COUNTRY_CODES;

    static {
        // Here I load city and country codes from the text file that I have on resources
        ITALIAN_CITY_CODES = loadCodes("/TownCodeList.txt");
        COUNTRY_CODES = loadCodes("/CountryCodeList.txt");
    }
    
    private static final String VOWEL_PATTERN = "[AEIOU]";
    private static final String CONSONANT_PATTERN = "[B-DF-HJ-NP-TV-Z]";
    private static final Pattern COMPILE_WOVELS = Pattern.compile(VOWEL_PATTERN);
    private static final Pattern COMPILE_CONSONANTS = Pattern.compile(CONSONANT_PATTERN);

    
    private static Map<String, String> loadCodes(String resourcePath) {
    	Map<String, String> codes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(PersonService.class.getResourceAsStream(resourcePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                codes.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error loading codes from " + resourcePath, e);
        }
        return codes;
    }

    public String generateCode(PersonInfo personInfo){
    	String lastnameCode = personInfo.getLastName();
        String nameCode = personInfo.getName();

        String birthDateCode = getBirthDateCode(personInfo.getDateOfBirth(), personInfo.getSex());
        String placeCode = getPlaceCode(personInfo.getHometown(), personInfo.getCountryOfBirth());
        
        if (placeCode == null) {
            return "Your place of birth doesn't exist in my data";
        }
               
        String partialCode = computeSurname(lastnameCode) + computeName(nameCode) + birthDateCode + placeCode;

        String a = computeControlChar(partialCode);
        
        return partialCode + a; //
    }

    
//    
    static String pickFirstThreeConsonants(String input) {
        Matcher m = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 3) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstTwoConsonantsAndFirstVowel(String input) {
        Matcher m = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 2) {
            result.append(m.group());
            cont++;
        }
        m = COMPILE_WOVELS.matcher(input);
        cont = 1;
        while (m.find() && cont <= 1) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstConsonantAndFirstTwoVowels(String input) {
        Matcher m = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 1) {
            result.append(m.group());
            cont++;
        }
        m = COMPILE_WOVELS.matcher(input);
        cont = 1;
        while (m.find() && cont <= 2) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstThreeVowels(String input) {
        Matcher m = COMPILE_WOVELS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 3) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstThirdAndFourthConsonant(String inputName) {
        Matcher m = COMPILE_CONSONANTS.matcher(inputName);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 4) {
            if (cont != 2) {
                result.append(m.group());
            }
            cont++;
        }
        return result.toString();
    }

    public static int howManyConsonants(String string) {
        StringBuilder match = new StringBuilder();
        
            string = string.toUpperCase();
            Pattern pattern = Pattern.compile("[B-DF-HJ-NP-TV-Z]+");
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                match.append(matcher.group());
            }
        return match.length();
    }
    
    public static int howManyVowels(String string) {
        StringBuilder match = new StringBuilder();
        
            string = string.toUpperCase();
            Pattern pattern = Pattern.compile("[AEIOU]+");
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                match.append(matcher.group());
            }
        return match.length();
    }
    
    public static String replaceSpecialChars(String input) {
        //If the name or surname include stressed letters (à,è,ì...) or other special characters (ä,ç,ß,...), it replaces them with corresponding letter (e.g. à = a)
        return input
                .toUpperCase()
                .replaceAll("[ÀÁÂÃÅĀ]", "A")
                .replaceAll("[ÄÆ]", "AE")
                .replaceAll("[ÈÉÊËĘĖĒ]", "E")
                .replaceAll("[ÌÍÎÏĮĪ]", "I")
                .replaceAll("[ÒÓÔÕOŌ]", "O")
                .replaceAll("[ÖŒØ]", "OE")
                .replaceAll("[ÙÚÛŪ]", "U")
                .replaceAll("[Ü]", "UE")
                .replaceAll("[ŚŠ]", "S")
                .replaceAll("ß", "SS")
                .replaceAll("[ÇĆČ]", "C")
                .replaceAll(" ", "")
                .replaceAll("-", "")
                .replaceAll("'", "");
    }


    public static String computeSurname(String lastnameCode) {
        String input = lastnameCode;
        input = replaceSpecialChars(input);

            StringBuilder result = new StringBuilder();
            input = input.toUpperCase();

            if (input.length() < 3) {
                result = new StringBuilder(input);
                while (result.length() < 3) {
                    result.append("X");
                }
            } else {
                switch (howManyConsonants(input)) {
                    case 0:
                        result.append(pickFirstThreeVowels(input));
                        break;
                    case 1:
                        result.append(pickFirstConsonantAndFirstTwoVowels(input));
                        break;
                    case 2:
                        result.append(pickFirstTwoConsonantsAndFirstVowel(input));
                        break;
                    default:
                        result.append(pickFirstThreeConsonants(input));
                }
            }
            return result.toString();
    }
    
    public static String computeName(String nameCode) {

        String inputName = replaceSpecialChars(nameCode);
            StringBuilder result = new StringBuilder();
            inputName = inputName.toUpperCase();

            if (inputName.length() < 3) {
                result = new StringBuilder(inputName);
                while (result.length() < 3)
                    result.append("X");
            } else {
                switch (howManyConsonants(inputName)) {
                    case 0:
                        result.append(pickFirstThreeVowels(inputName));
                        break;
                    case 1:
                        result.append(pickFirstConsonantAndFirstTwoVowels(inputName));
                        break;
                    case 2:
                        result.append(pickFirstTwoConsonantsAndFirstVowel(inputName));
                        break;
                    case 3:
                        result.append(pickFirstThreeConsonants(inputName));
                        break;
                    default:
                        result.append(pickFirstThirdAndFourthConsonant(inputName));
                }
            }
            return result.toString();
    }


    private String getBirthDateCode(LocalDate birthDate, String sex) {
    	int day = birthDate.getDayOfMonth();
    	int month = birthDate.getMonthValue();
    	int year = birthDate.getYear();
        String result = "";

    	                    // to get the last 2 digits of the year
    	                    if (year % 100 >= 10) {
    	                        result += (year % 100);
    	                    } else {
    	                        result = result + 0 + (year % 100);
    	                    }

    	                    // to get the letter corresponding to the month
    	                    switch (month) {
    	                        case 1: {
    	                            result += "A";
    	                            break;
    	                        }
    	                        case 2: {
    	                            result += "B";
    	                            break;
    	                        }
    	                        case 3: {
    	                            result += "C";
    	                            break;
    	                        }
    	                        case 4: {
    	                            result += "D";
    	                            break;
    	                        }
    	                        case 5: {
    	                            result += "E";
    	                            break;
    	                        }
    	                        case 6: {
    	                            result += "H";
    	                            break;
    	                        }
    	                        case 7: {
    	                            result += "L";
    	                            break;
    	                        }
    	                        case 8: {
    	                            result += "M";
    	                            break;
    	                        }
    	                        case 9: {
    	                            result += "P";
    	                            break;
    	                        }
    	                        case 10: {
    	                            result += "R";
    	                            break;
    	                        }
    	                        case 11: {
    	                            result += "S";
    	                            break;
    	                        }
    	                        case 12: {
    	                            result += "T";
    	                            break;
    	                        }
    	                    }
    	                    switch (sex.toLowerCase()) {
    	                        case "female": {
    	                            result += (day + 40);
    	                            break;
    	                        }
    	                        case "male": {
    	                            result += (day <= 10 ? "0" + day : day);
    	                            break;
    	                        }
    	                    }
        return result;
    } 
    //
    
    public static String getPlaceCode(String hometown, String countryOfBirth) {
        String placeCode = null;
        String townString = hometown.toUpperCase();
        String countryString = countryOfBirth.toUpperCase();
        
        if (!"ITALY".equals(countryString)) {
            placeCode = COUNTRY_CODES.get(countryString);
        } else {
            placeCode = ITALIAN_CITY_CODES.get(townString);
            if (placeCode == null) {
                // If town is not found in Italy, return an error message
                return "Invalid town: " + townString;
            }
        }
        return placeCode;
    }


    public Integer isPlaceCodeZ(String placeCode) {
        // Check if the first character is 'Z'
    	if (placeCode.length() > 20) {
    		return 20;
    	}
        else {
        if (placeCode.charAt(11) == 'Z') {
        	return 1;
        } else {
            return 0;
        }
        }
    	
    }

    
    public String computeControlChar(String code) {
        String control = "";
        int evenSum = 0, oddSum = 0;
        code = code.toUpperCase();
        if (code.length() == 15) {
            OddThread ot = new OddThread(code, oddSum);
            Thread t1 = new Thread(ot);
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                return ""; 
            }
            oddSum = ot.getOddSum();

            EvenThread et = new EvenThread(code, evenSum);
            Thread t2 = new Thread(et);
            t2.start();
            try {
                t2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                
                return ""; 
                }
            evenSum = et.getEvenSum();

            // Now we handle with control character
            int sum = (oddSum + evenSum) % 26;
            switch (sum) {
                case 0: control = "A"; break;
                case 1: control = "B"; break;
                case 2: control = "C"; break;
                case 3: control = "D"; break;
                case 4: control = "E"; break;
                case 5: control = "F"; break;
                case 6: control = "G"; break;
                case 7: control = "H"; break;
                case 8: control = "I"; break;
                case 9: control = "J"; break;
                case 10: control = "K"; break;
                case 11: control = "L"; break;
                case 12: control = "M"; break;
                case 13: control = "N"; break;
                case 14: control = "O"; break;
                case 15: control = "P"; break;
                case 16: control = "Q"; break;
                case 17: control = "R"; break;
                case 18: control = "S"; break;
                case 19: control = "T"; break;
                case 20: control = "U"; break;
                case 21: control = "V"; break;
                case 22: control = "W"; break;
                case 23: control = "X"; break;
                case 24: control = "Y"; break;
                case 25: control = "Z"; break;
            }
        }
        return control;
    }
}
