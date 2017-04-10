package pseudo;

import java.io.IOException;
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
            return getTag(character);
        }
    }


    private Lexeme getTag(Character ch) throws IOException, TokenizerException {

        switch(ch) {
            case '<': return extractTag(ch);
            case '>': return extractTag(ch);
            default: throw new TokenizerException("Unknown character: " + String.valueOf(ch));

        }
    }

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

    private Lexeme extractHealthCareUnit() throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        while (Character.isLetter(scanner.current())) {
            strBuilder.append(scanner.current());
            scanner.moveNext();
        }
        return new Lexeme(strBuilder.toString(), Token.HEALTH_CARE_UNIT);
    }

    private Lexeme extractLocation() throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        while (Character.isLetter(scanner.current())) {
            strBuilder.append(scanner.current());
            scanner.moveNext();
        }
        return new Lexeme(strBuilder.toString(), Token.LOCATION);
    }

 /*   private Lexeme extractInt() throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        while (Character.isDigit(scanner.current())) {
            strBuilder.append(scanner.current());
            scanner.moveNext();
        }
        return new Lexeme(strBuilder.toString(), Token.INT_LIT);
    }

    private Lexeme addSubOP(Character ch) throws IOException {
        Lexeme lexeme = null;
        StringBuilder strBuilder = new StringBuilder();
        if (ch == '+') {
            while (Character.getType(scanner.current()) == Character.MATH_SYMBOL) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
                lexeme = new Lexeme(strBuilder.toString(), Token.ADD_OP);
            }

        } else if (ch == '-') {
            while (Character.getType(scanner.current()) == Character.DASH_PUNCTUATION) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
                lexeme = new Lexeme(strBuilder.toString(), Token.SUB_OP);
            }
        }
        return lexeme;
    }


    private Lexeme mulDivOP(Character ch) throws IOException {
        Lexeme lexeme = null;
        StringBuilder strBuilder = new StringBuilder();
        while (Character.getType(scanner.current()) == Character.OTHER_PUNCTUATION) {
            if (ch == '*') {
                strBuilder.append(scanner.current());
                scanner.moveNext();
                lexeme = new Lexeme(strBuilder.toString(), Token.MULT_OP);
            } else if (ch == '/') {
                strBuilder.append(scanner.current());
                scanner.moveNext();
                lexeme = new Lexeme(strBuilder.toString(), Token.DIV_OP);
            }
        }
        return lexeme;
    }*/

    private Lexeme extractTag(Character ch) throws IOException {
        Lexeme lexeme = null;
        StringBuilder strBuilder = new StringBuilder();

        if (ch == '<') {
            while(Character.getType(scanner.current()) == Character.START_PUNCTUATION) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
                lexeme = new Lexeme(strBuilder.toString(), Token.LEFT_TAG);
                typeOfTag(ch);

            }
        } else if (ch == '>') {
            while(Character.getType(scanner.current()) == Character.END_PUNCTUATION) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
                lexeme = new Lexeme(strBuilder.toString(), Token.RIGHT_TAG);
            }
        }
        return lexeme;
    }

    private void typeOfTag(Character ch) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        if (ch == 'H') {
            extractHealthCareUnit();
        } else if (ch == 'L') {
            while (Character.isLetter(scanner.current())) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
            }
            if (strBuilder.toString().equals("Location")) {
                extractLocation();
            } else {
                scanner.moveNext();
            }
        }
    }

    public void close() throws IOException {
        if (scanner != null)
            scanner.close();
    }
}


