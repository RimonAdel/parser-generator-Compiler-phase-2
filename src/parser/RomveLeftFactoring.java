package parser;

import java.io.FileNotFoundException;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.HashMap;

public class RomveLeftFactoring {

    final static String filePath = "CFG grammer3.txt";

    public static void main(String[] args) {
        ArrayList<String> terminals;
        ArrayList<String> nonTerminals;
        HashMap<String, ArrayList<String>> inputCFG;
        HashMap<String, ArrayList<String>> tempLL1 = new HashMap<>();

        //Step 1: Read CFG and find terminals and non-terminals
        CFGrammer dealWithGrammer = new CFGrammer(filePath);
        try {
            dealWithGrammer.dealWithFile();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }
        terminals = dealWithGrammer.getTerminals();
        terminals.add("$");
        nonTerminals = dealWithGrammer.getNonTerminals();
        inputCFG = dealWithGrammer.getCfg();
        // Note : \L indicates epsilons
        System.out.println("\ncfg:\n" + inputCFG);
        System.out.println("--------------------------------------------------------------------");

        System.out.println("\nleft recursion free cfg:\n" + inputCFG);
        System.out.println("--------------------------------------------------------------------");
        HashMap<String,ArrayList<String>> fistStage = elminateLeftRecusion2(inputCFG, terminals, nonTerminals);
        System.out.println(fistStage);

    }


    public static HashMap<String, ArrayList<String>> elminateLeftRecusion(HashMap<String, ArrayList<String>> inputCFG, ArrayList<String> terminals, ArrayList<String> nonTerminals) {
        HashMap<String, ArrayList<String>> returnGrammer = new HashMap<>();
        for (String key : inputCFG.keySet()) {
            ArrayList<String> nonLeftRec = new ArrayList<>();
            ArrayList<String> leftRec = new ArrayList<>();
            boolean hasLeftRec = false;
            for (String term : inputCFG.get(key)) {
                String[] subterm = term.split("\\s+");
                if (subterm[0].equals(key)) {
                    hasLeftRec = true;
                    if (term.indexOf(key) + + key.length() < term.length()) {
                        String temp = term.substring(term.indexOf(key) + key.length(), term.length());
                        leftRec.add(temp + " " + key + "'");
                    }
                } else if (hasLeftRec) {
                    nonLeftRec.add(term + " " + key + "'");
                } else {
                    nonLeftRec.add(term);
                }
            }
            if (leftRec.size() > 0) {
                leftRec.add("\\l");
                returnGrammer.put(key + "'", leftRec);
            }
            if (nonLeftRec.size() > 0) {
                returnGrammer.put(key, nonLeftRec);
            }
        }
        return returnGrammer;
    }


    public static HashMap<String, ArrayList<String>> elminateLeftRecusion2(HashMap<String, ArrayList<String>> inputCFG, ArrayList<String> terminals, ArrayList<String> nonTerminals) {
        HashMap<String, ArrayList<String>> returnGrammer = new HashMap<>();
        for (String key : inputCFG.keySet()) {
            ArrayList<String> nonLeftRec = new ArrayList<>();
            ArrayList<String> leftRec = new ArrayList<>();
            boolean hasLeftRec = false;
            for (String term : inputCFG.get(key)) {
                String[] subterm = term.split("\\s+");
                if (subterm[0].equals(key)) {
                    hasLeftRec = true;
                    break;
                }
            }
                if (hasLeftRec) {
                    for (String term : inputCFG.get(key)) {
                        String[] subterm = term.split("\\s+");
//                        if (subterm[0].equals(key)) {
                            hasLeftRec = true;
                            if (term.indexOf(key) + +key.length() < term.length()) {
                                System.out.println("no");
                                String temp = term.substring(term.indexOf(key) + key.length(), term.length());
                                leftRec.add(temp + " " + key + "'");
                            } else {
                                System.out.println("yes");
                                nonLeftRec.add(term + " " + key + "'");
                            }
                        }
//                    }

                    if (leftRec.size() > 0) {
                        leftRec.add("\\l");
                        returnGrammer.put(key + "'", leftRec);
                    }
                    if (nonLeftRec.size() > 0) {
                        returnGrammer.put(key, nonLeftRec);
                    }

                } else {
                    returnGrammer.put(key, inputCFG.get(key));
                }
            }
        return returnGrammer;
    }

    public static HashMap<String, ArrayList<String>> elminateLeftRecurentRecusion(HashMap<String, ArrayList<String>> inputCFG, ArrayList<String> termianls, ArrayList<String> nonTerminals) {
        HashMap<String, ArrayList<String>> returnGrammer = inputCFG;
        for (String key : returnGrammer.keySet()) {
            boolean sub = false;
            ArrayList<String> nonLeftRec = new ArrayList<>();
            ArrayList<String> leftRec = new ArrayList<>();
            for (String term : returnGrammer.get(key)) {
                String[] subterm = term.split("\\s+");
                if (nonTerminals.contains(subterm[0])) {
                    for (String tempterm : returnGrammer.get(subterm[0])) {
                        String[] tempSubterm = tempterm.split("\\s+");
//                        System.out.println("tempSubterm[0] "+tempSubterm[0]+"key"+key+" "+(tempSubterm[0].equals(key))+ " "+"tempSubterm[tempSubterm.length-1]= "+tempSubterm[tempSubterm.length-1]+"subterm[0]+\"'\""+subterm[0]+"'"+ tempSubterm[tempSubterm.length-1].equals(subterm[0]+"'"));
                        if (tempSubterm[0].equals(key) && tempSubterm[tempSubterm.length - 1].equals(subterm[0] + "'")) {
                            sub = true;
                            break;
                        }
                    }
                    if (sub) {
                        ArrayList<String> newGrammer = new ArrayList<>();
                        for (String tempterm : returnGrammer.get(subterm[0])) {
                            String[] tempSubterm = tempterm.split("\\s+");
                            if (tempSubterm[0].equals(key)) {
                                for (String subString : returnGrammer.get(key)) {
                                    String newGrammerString = subString + tempterm.substring(tempterm.indexOf(key) + key.length(), tempterm.length());
                                    newGrammer.add(newGrammerString);
                                }
                            } else {
                                newGrammer.add(tempterm);
                            }
                        }
                        returnGrammer.put(subterm[0], newGrammer);
                    }

                }
            }

        }
        return returnGrammer;
    }
}