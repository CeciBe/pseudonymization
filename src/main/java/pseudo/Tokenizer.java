package pseudo;

import java.io.*;
import java.util.*;

class Tokenizer {

    private static HashMap<Object, Token> symbols = null;

    private Scanner scanner = null;
    private Lexeme current = null;
    private Lexeme next = null;


    Tokenizer(){
        symbols = new HashMap<Object, Token>();
        symbols.put(Scanner.EOF, Token.EOF);
    }


    public void open(String fileName) throws IOException, TokenizerException {
        scanner = new Scanner();
        scanner.open(fileName);
        scanner.moveNext();
        next = extractLexeme();
        symbols.put(next.value(), next.token());
    }

    public Lexeme current() {
        return current;
    }


    public void moveNext() throws IOException, TokenizerException {
        if (scanner == null)
            throw new IOException("No open file");
        current = next;
        if (next.token() != Token.EOF)
            next = extractLexeme();
        symbols.put(next.value(), next.token());
    }

    private Lexeme extractLexeme() throws IOException, TokenizerException {
        consumeWhiteSpaces();
        Character character = scanner.current();
        if (character == Scanner.EOF) {
            return new Lexeme(character, Token.EOF);
        } else if (Character.isLetter(character)) {
            return extractLetter();
        //} else if (Character.isDigit(character)) {
            //return extractInt();
        } else if (symbols.containsKey(character)) {
            scanner.moveNext();
            return new Lexeme(character, symbols.get(character));
        } else {
            return extractTag(character);
        }
    }


    /***private Lexeme getTag(Character ch) throws IOException, TokenizerException {

       switch(ch) {
            case '<': return extractTag(ch);
            //case '>': return extractTag(ch);
            default: throw new TokenizerException("Unknown character: " + String.valueOf(ch));

        }
    } ***/

    private void consumeWhiteSpaces() throws IOException {
        while (Character.isWhitespace(scanner.current())) {
            scanner.moveNext();
        }
    }

    private Lexeme extractLetter() throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        while (Character.isLetter(scanner.current())) {
            strBuilder.append(scanner.current());
            scanner.moveNext();
        }
        return new Lexeme(strBuilder.toString(), Token.HEALTH_CARE_UNIT);
    }

    private Lexeme extractHealthCareUnit(StringBuilder strBuilder) throws IOException {
        Lexeme HCU_Lexeme = null;
        while (Character.getType(scanner.current()) == '_' || Character.isLetter(scanner.current())) {
            strBuilder.append(scanner.current());
            scanner.moveNext();
            if (scanner.current() == '>'){
                strBuilder.append(scanner.current());
                break;
            }
        }
        if(strBuilder.toString().equals("<Health_Care_Unit>")) {
            HCU_Lexeme = new Lexeme(strBuilder.toString(), Token.STARTTAG_HCU);
        } else if (strBuilder.toString().equals("</Health_Care_Unit>")) {
            HCU_Lexeme = new Lexeme(strBuilder.toString(), Token.ENDTAG_HCU);
        }
        return HCU_Lexeme;
    }

    private Lexeme extractLocation(StringBuilder strBuilder) throws IOException {
        Lexeme lexeme = null;
        while (Character.isLetter(scanner.current())) {
            strBuilder.append(scanner.current());
            scanner.moveNext();
            if (scanner.current() == '>'){
                strBuilder.append(scanner.current());
                break;
            }
        }
        if(strBuilder.toString().equals("<Location>")){
            lexeme = new Lexeme(strBuilder.toString(), Token.STARTTAG_L);
        } else if (strBuilder.toString().equals("</Location>")) {
            lexeme = new Lexeme(strBuilder.toString(), Token.ENDTAG_L);
        } else {
            lexeme = new Lexeme(strBuilder.toString(), Token.OTHER_TAG);
        }
        return lexeme;
    }

    private Lexeme extractTag(Character ch) throws IOException {
        Lexeme lexeme = null;
        StringBuilder strBuilder = new StringBuilder();
        if (ch == '<') {
           // while(Character.getType(scanner.current()) == Character.START_PUNCTUATION) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
                ch = scanner.current();
                lexeme = checkStartOrEnd(ch, strBuilder);
            //}
        }
        return lexeme;
    }

    private Lexeme checkStartOrEnd(Character ch, StringBuilder strBuilder) throws IOException {
        Lexeme result = null;
        if (ch == '/') {
            strBuilder.append(scanner.current());
            scanner.moveNext();
            ch = scanner.current();
            result = typeOfTag(ch, strBuilder);
        } else {
            result = typeOfTag(ch, strBuilder);
        }
        return result;
    }

    private Lexeme typeOfTag(Character ch, StringBuilder strBuilder) throws IOException {
        //StringBuilder strBuilder = new StringBuilder();
        Lexeme lexeme = null;
        if (ch == 'H') {
            lexeme = extractHealthCareUnit(strBuilder);
        } else if (ch == 'L') {
            lexeme = extractLocation(strBuilder);
        } else {
            scanner.moveNext();
            return null;
        }
        return lexeme;
    }

    public void close() throws IOException {
        if (scanner != null)
            scanner.close();
    }
}


