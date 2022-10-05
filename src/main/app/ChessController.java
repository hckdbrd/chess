import javax.swing.*;

public class ChessController {
   public static void main(String[] args) {
      JFrame frame = new JFrame("Chess - GAMBIT");
      frame.setSize(600,600);

      ChessView panel = new ChessView();
      frame.add(panel);

      frame.setVisible(true);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   }
}
