import javax.swing.*;

public class Application {
   public static void main(String[] args) {
      JFrame frame = new JFrame("Chess - GAMBIT");
      frame.setSize(600,600);

      ChessPanel panel = new ChessPanel();
      frame.add(panel);

      frame.setVisible(true);
   }
}
