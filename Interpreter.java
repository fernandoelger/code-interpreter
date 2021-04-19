import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Interpreter {

  private static byte [] memory;

  private int memoryPointer;
  private int programPointer;
  private int ifPointer;

  private String sourceCode;
  private String ifFile;


  public Interpreter() throws IOException, FileNotFoundException  {
    memory = new byte[1000];

    for (int i = 0; i < 1000; i++) {
      memory[i] = 0;
    }

    memoryPointer = 0; 
    programPointer = 0;
    ifPointer = 0;

    // lê arquivo source
    Path path = Path.of("files/source.txt/");
    try {
      sourceCode = Files.readString(path);
      //ignora line breaks, deixando arquivo em somente uma linha
      sourceCode = sourceCode.replace(System.getProperty("line.separator"), "");
    } catch (IOException e){
      throw new IOException("'source.txt' not found in files directory when trying to read it.");
      }

    // lê arquivo if
    path = Path.of("files/if.txt/");
    try {
      ifFile = Files.readString(path);
      //ignora line breaks, deixando arquivo em somente uma linha
      ifFile = ifFile.replace(System.getProperty("line.separator"), "");
    } catch (IOException e){
      throw new IOException("'if.txt' not found in files directory when trying to read it.");
      }
  }

  public void run() throws IOException {
    while (!(sourceCode.charAt(programPointer) == '$')) {
      switch (sourceCode.charAt(programPointer)) {
        
        case '>':
          System.out.println(sourceCode.charAt(programPointer));
          memoryPointer++;
          programPointer++;
          break;
  
        case '<':
          System.out.println(sourceCode.charAt(programPointer));
          memoryPointer--;
          programPointer++;
          break;
  
        case '+':
          System.out.println(sourceCode.charAt(programPointer));
          memory[memoryPointer] = (byte) (memory[memoryPointer] + 1);
          programPointer++;
          break;
  
        case '-':
          System.out.println(sourceCode.charAt(programPointer));
          memory[memoryPointer] = (byte) (memory[memoryPointer] - 1);
          programPointer++;
          break;
  
        case '[':
          System.out.println(sourceCode.charAt(programPointer));
          //System.out.println("mem pointer: " + memoryPointer);
          //System.out.println("mem value: " + memory[memoryPointer]);
          if (memory[memoryPointer] == 0) {
            char value = sourceCode.charAt(++programPointer); 
            while (!(value == ']')) {
              value = sourceCode.charAt(++programPointer);
            }
            programPointer++;
          } else {
            programPointer++;
          }
          break;
  
        case ']':
          System.out.println(sourceCode.charAt(programPointer));
          //System.out.println("mem pointer: " + memoryPointer);
          //System.out.println("mem value: " + memory[memoryPointer]);
          if (!(memory[memoryPointer] == 0)) {
            char value = sourceCode.charAt(--programPointer); 
            while (!(value == '[')) {
              value = sourceCode.charAt(--programPointer);
            }
            programPointer++;
          } else {
            programPointer++;
          }
          break;

        case ',':
          System.out.println(sourceCode.charAt(programPointer));
          int inputValue;
          try {
            inputValue = ifFile.charAt(ifPointer++);
          } catch (NullPointerException e) {
              throw new NullPointerException("Tried do read input in 'if.file', but there is no more inputs.");
          }
          memory[memoryPointer] = (byte) inputValue;
          programPointer++;
          break;

        case '.':
          System.out.println(sourceCode.charAt(programPointer));
          byte outputValue = memory[memoryPointer];
          String stringConvertion = outputValue + " ";
          write(stringConvertion);
          programPointer++;
          break;
  
        default:
          break;
      }
    }
  }

  private void write(String s) throws IOException {
    try {
      Files.write(Paths.get("files/of.txt"), s.getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
        throw new IOException("'of.txt' not found in files directory when trying to write on it.");
      }
  }
}
