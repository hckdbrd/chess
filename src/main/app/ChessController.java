import backend.pieces.ChessPiece;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

import javax.swing.*;

public class ChessController implements ChessDelegate, ActionListener {
   private final String SOCKET_SERVER_ADDR = "192.168.31.133"; //localhost
   private final int PORT = 50000;

   private final ChessModel chessModel = new ChessModel();

   private final JFrame frame;
   private final ChessView chessBoardPanel;
   private final JButton resetBtn;
   private final JButton serverBtn;
   private final JButton clientBtn;

   private ServerSocket listener;
   private Socket socket;
   private PrintWriter printWriter;

   ChessController() {
      chessModel.reset();

      frame = new JFrame("Chess - GAMBIT");
      frame.setSize(500, 550);
      frame.setLocationRelativeTo(null);
      frame.setLayout(new BorderLayout());

      chessBoardPanel = new ChessView(this);

      frame.add(chessBoardPanel, BorderLayout.CENTER);

      var buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      resetBtn = new JButton("Reset");
      resetBtn.addActionListener(this);
      buttonsPanel.add(resetBtn);

      serverBtn = new JButton("Host");
      serverBtn.addActionListener(this);
      buttonsPanel.add(serverBtn);

      clientBtn = new JButton("Connect");
      clientBtn.addActionListener(this);
      buttonsPanel.add(clientBtn);

      frame.add(buttonsPanel, BorderLayout.PAGE_END);

      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.addWindowListener(new WindowAdapter() {
         @Override
         @SneakyThrows
         public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            if (printWriter != null) printWriter.close();
            if (listener != null) listener.close();
            if (socket != null) socket.close();
         }
      });
   }

   public static void main(String[] args) {
      new ChessController();
   }

   @Override
   public ChessPiece pieceAt(int col, int row) {
      return chessModel.pieceAt(col, row);
   }

   @Override
   public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
      chessModel.movePiece(fromCol, fromRow, toCol, toRow);
      chessBoardPanel.repaint();
      if (printWriter != null) {
         printWriter.println(fromCol + "," + fromRow + "," + toCol + "," + toRow);
      }
   }

   private void receiveMove(Scanner scanner) {
      while (scanner.hasNextLine()) {
         var moveStr = scanner.nextLine();
         System.out.println("WARNING: Chess move received: " + moveStr);
         var moveStrArr = moveStr.split(",");
         var fromCol = Integer.parseInt(moveStrArr[0]);
         var fromRow = Integer.parseInt(moveStrArr[1]);
         var toCol = Integer.parseInt(moveStrArr[2]);
         var toRow = Integer.parseInt(moveStrArr[3]);
         SwingUtilities.invokeLater(() -> {
            chessModel.movePiece(fromCol, fromRow, toCol, toRow);
            chessBoardPanel.repaint();
         });
      }
   }

   private void runSocketServer() {
      Executors.newFixedThreadPool(1).execute(new Runnable() {
         @Override
         @SneakyThrows
         public void run() {
            listener = new ServerSocket(PORT);
            System.out.println("WARNING: Server is hosted on port " + PORT);
            socket = listener.accept();
            System.out.println("Connected from " + socket.getInetAddress());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            var scanner = new Scanner(socket.getInputStream());
            receiveMove(scanner);
         }
      });
   }

   @SneakyThrows
   private void runSocketClient() {
      socket = new Socket(SOCKET_SERVER_ADDR, PORT);
      System.out.println("Client connected to port " + PORT);
      var scanner = new Scanner(socket.getInputStream());
      printWriter = new PrintWriter(socket.getOutputStream(), true);
      Executors.newFixedThreadPool(1).execute(() -> receiveMove(scanner));
   }

   @Override
   @SneakyThrows
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == resetBtn) {
         chessModel.reset();
         chessBoardPanel.repaint();
         if (listener != null) {
            listener.close();
         }
         if (socket != null) {
            socket.close();
         }
         serverBtn.setEnabled(true);
         clientBtn.setEnabled(true);
      } else if (e.getSource() == serverBtn) {
         serverBtn.setEnabled(false);
         clientBtn.setEnabled(false);
         frame.setTitle("Chess - GAMBIT [Server]");
         runSocketServer();
         JOptionPane.showMessageDialog(frame, "listening on port " + PORT);
      } else if (e.getSource() == clientBtn) {
         serverBtn.setEnabled(false);
         clientBtn.setEnabled(false);
         frame.setTitle("Chess - GAMBIT [Client]");
         runSocketClient();
         JOptionPane.showMessageDialog(frame, "connected to port " + PORT);
      }
   }
}