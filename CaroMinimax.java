/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.carominimax;

import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;


/**
 *
 * @author nhan2
 */
public class CaroMinimax extends JFrame implements MouseListener{
    
    private JPanel mainP, p1, p2;
    private JButton[][] arBT;
    private int[][] arr;
    private boolean[][] arrEnd;
    private JButton btnNewGame, btnExit, btnInfo, btnHelp, btnUndo;
    private boolean turn;
    private boolean ketthucgame;
    
    //ButtonUndo
    private Move[] arrUndo;
    private int undo;
    
    //Tạo bảng điểm
    private JLabel lb1, lb2, lb3;
    private int may, nguoi, hoa;
    
    
    public CaroMinimax(){
        
        arBT = new JButton[3][3];
        arr = new int[3][3];
        arrEnd = new boolean[3][3];
        
        //Khai báo mảng Undo
        arrUndo = new Move[9];
        
        TaoGUI();
        
        
        setTitle("Game caro - nhóm 15");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void addP1(){
        mainP.add(p1 = new JPanel(), BorderLayout.CENTER);
        p1.setLayout(new GridLayout(3,3, 5,5));
        for (int i = 0; i < arBT.length; i++) {
            for (int j = 0; j < arBT[i].length; j++) {
                arBT[i][j] = new JButton();
                arBT[i][j].addMouseListener(this);
                arBT[i][j].setPreferredSize(new Dimension(100, 100));
                arBT[i][j].setBackground(new Color(0, 10, 10));
           
                arBT[i][j].setFont(new Font("Arial",Font.BOLD,24));
                p1.add(arBT[i][j]);
            }
        }
    }
    
    public void newgame() {
        for (int i = 0; i < arBT.length; i++) {
            for (int j = 0; j < arBT[i].length; j++) {
                arBT[i][j].setText("");
                arr[i][j] = 0;
                arrEnd[i][j] = false;
                turn = false;
                ketthucgame = false;
                undo = 0;
            }
        }
    }
   
    
    private void TaoGUI(){
        
        add(mainP = new JPanel());
        addP1();
        
        mainP.add(p2 = new JPanel(), BorderLayout.SOUTH); 
        p2.setPreferredSize(new Dimension(400,200));
        
        p2.add(new JLabel("Bảng điểm [ "));
        p2.add(new JLabel("Người : "));
        p2.add(lb1 = new JLabel("   0 "));
        p2.add(new JLabel("Máy : "));
        p2.add(lb2 = new JLabel("   0 "));
        p2.add(new JLabel("Hòa : "));
        p2.add(lb3 = new JLabel("   0 "));
        p2.add(new JLabel("]"));
          
        p2.add(btnNewGame = new JButton("Game mới"));
        btnNewGame.setEnabled(false);
        
        
        
        //set sự kiện button newgame
        btnNewGame.setBackground(new Color(230, 240, 240));
        btnNewGame.setForeground(Color.red);
        btnNewGame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                newgame();
                //lb1.setText(nguoi+" ");
                //lb2.setText(may+" ");
                //lb3.setText(hoa+" ");
                btnNewGame.setEnabled(false);
            }
        });
        
        
        //Button Undo
        p2.add(btnUndo = new JButton("Đánh lại"));
        btnUndo.setEnabled(false); //khi chưa đánh sẽ không hiện thị button undo
        //Khởi tạo sự kiện undo để xóa đi nước đánh của người và máy
        btnUndo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 2; i++) {
                    //Vì mỗi lần đánh là 2 nước đánh của người và máy
              //Nên khi dùng undo-- sẽ xóa nước đi của máy 
              //Và sau đó gọi Move để xóa tiếp nước đi của người
              undo--;
              Move move = arrUndo[undo];
              //Lấy vị trí đánh hiện tại là xóa nó đi 
              arBT[move.getX()][move.getY()].setText(""); 
              //Nếu undo đi thì có thể đánh lại được tại vị trí đã undo trước đó
              //Vì thế set arrEnd[][] = false
              arrEnd[move.getX()][move.getY()] = false;
              arr[move.getX()][move.getY()] = 0;//Gán vị trí mảng = 0 || là nước đi trống
                }   
                //Khi bấm vễ hết rồi thì sẽ ẩn nút
                if(undo == 0) btnUndo.setEnabled(false);
            }
        
        });
       
        
        //Button Helps
        p2.add(btnHelp = new JButton("Trợ giúp"));  
        btnHelp.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(mainP,
                                "Luật chơi rất đơn giản bạn chỉ cần 3 ô liên tiếp nhau\n"
                                        + "Theo hàng ngang hoặc dọc hoặc chéo là bạn đã thắng", "Luật Chơi",
                                JOptionPane.CLOSED_OPTION);
            }
        });
        
        
        //Button Info
        p2.add(btnInfo = new JButton("Thông tin")); 
        btnInfo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                   //Object[] options = {"OK"};
                 JOptionPane.showConfirmDialog(mainP,
                                "Nguyễn Thành Nhân_21DH113932\nTrần Đình Tài_MSSV\nTrương Gia Huy_MSSV\nTrần Khánh Duy_MSSV", "Information",
                                JOptionPane.CLOSED_OPTION); 
            }
        });
         p2.add(btnExit = new JButton("Thoát")); 
        btnExit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        
        });
    }
    
    
    //Phuong thức nếu thắng thì sẽ tự cộng vào bảng điểm luôn
    public void diem(){
        lb1.setText(nguoi+" ");
        lb2.setText(may+" ");
        lb3.setText(hoa+" ");
    }
    
    
     public int CheckWin(int[][] arr){
         
        //Kiểm tra hàng
        for (int i = 0; i < arr.length; i++) {
            int a = arr[i][0]; 
            if (a == arr[i][1] && arr[i][1] == arr[i][2]) { // So sánh bằng 
                if (a == 1) { // Nếu X thắng thì trả về -10
                    return -10;
                } else if (a == 2) { // Nếu O thắng thì trả về 10
                    return 10;
                }
            }
        }
        
        // Kiểm tra cột
         for (int i = 0; i < arr.length; i++) {
             int a = arr[0][i]; 
             if (a == arr[1][i] && arr[1][i] == arr[2][i]) {
                 if (a == 1) { // Nếu X thắng thì trả về -10
                    return -10;
                } else if (a == 2) { // Nếu O thắng thì trả về 10
                    return 10;
                }
             }
         }
         
         //Kiểm tra chéo trái
         if (arr[0][0] == arr[1][1] && arr[0][0] == arr[2][2]) { 
              if (arr[0][0] == 1) { // Nếu X thắng thì trả về -10
                    return -10;
                } else if (arr[0][0] == 2) { // Nếu O thắng thì trả về 10
                    return 10;
                }
            }  
         
         //Kiểm tra chéo phải
          if (arr[2][0] == arr[1][1] && arr[2][0] == arr[0][2]) { 
              if (arr[2][0] == 1) { // Nếu X thắng thì trả về -10
                    return -10;
                } else if (arr[2][0] == 2) { // Nếu O thắng thì trả về 10
                    return 10;
                }
            }  
          
          //Nếu không thỏa điều kiện nào thì trả về false tức là 0
          return 0;
        }
     
     //Khởi tạo phương thức kiểm tra coi nước đi đó có tồn tại hay không ?
     public boolean checkMove(int[][] arr){
         for (int i = 0; i < arr.length; i++) {
             for (int j = 0; j < arr[i].length; j++) {
                 if (arr[i][j] == 0)  return true;// Nếu ô = 0 chứng tổ ô đó chưa được đ             
             }
         }
         //Nếu đã đánh hết ô thì trả về false
         return false;
     }
     
     //Khởi tạo phương thức tìm đường đi cho bot
     public int minimax(int[][] arr, boolean isTurn){
        int s = CheckWin(arr);
         if (s == 10) return s; //Máy thắng
         if (s == -10) return s; // Người thắng 
         if(checkMove(arr) == false) return 0; // hòa 
         if(isTurn == true) { // Lượt đánh của máy
             int best = -10000; 
             for (int i = 0; i < arr.length; i++) {
                 for (int j = 0; j < arr[i].length; j++) {
                     if (arr[i][j] == 0) { //nếu chạy hết mảng
                         arr[i][j] = 2;
                         //Thực hiện gọi đệ quy minimax để so sanh với biến best
                         //nếu khi máy đánh 1 ô thì nó sẽ gán và 2 sau đó chạy về hàm ban đầu để kiểm tra chiến thắng
                         //Kiểm tra nếu thắng thì giá trị = 10 || so sanh bằng hàm max ta có (-10000 với 10) max là 10
                         //Sau đó máy sẽ thực hiện đánh vô đường đi max 10
                         best = Math.max(best, minimax(arr, !isTurn)); // lựa chọn giá trị lớn nhất để đánh nước tiếp theo
                         arr[i][j] = 0;
                     }
                 }
             }
             return best;
             
           
         } else { // với lượt đánh của người 
             int best = 10000;
             for (int i = 0; i < arr.length; i++) {
                 for (int j = 0; j < arr[i].length; j++) {
                     if (arr[i][j] == 0) {
                         arr[i][j] = 1;
                         best = Math.min(best, minimax(arr, !isTurn));
                         arr[i][j] = 0;
                     }
                 }
             }
             return best;
         }
     }
     
     //turn = true khi đánh X, false khi đánh O
     
     public Move bestMove(int[][] arr, boolean turn){ 
         int best = -1000;
         Move result = new Move(-1, -1); // tọa độ điểm dánh
         //Chạy hết mảng
         for (int i = 0; i < arr.length; i++) {
             for (int j = 0; j < arr[i].length; j++) {
                 if(arr[i][j] == 0){ 
                     arr[i][j] = 2;
                     int score = minimax(arr, !turn);
                     arr[i][j] = 0;
                     if (score > best) {
                        best = score;
                        result.setX(i);
                        result.setY(j);
                        //result.x = i;
                        //result.y = j;
                         
                     }
                 }
             }
         }
         return result;
     }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < arBT.length; i++) {
            for (int j = 0; j < arBT[i].length; j++) {
                //Điều kiện này để kiểm tra khi click thì sẽ không được click lại nữa
                //Điều kiện tiếp là tới lượt thì mới được đánh thì turn = true
                if (e.getButton() == 1 && e.getSource() == arBT[i][j] && turn == false && arrEnd[i][j] == false) {
                    arBT[i][j].setText("X");
                    arr[i][j] = 1;
                    arBT[i][j].setForeground(Color.red);
                    arrEnd[i][j] = true;
                    turn = true;
                    btnNewGame.setEnabled(true);
                    
                    Move move = new Move(i, j);
                    arrUndo[undo] = move;
                    undo++;
                    btnUndo.setEnabled(true); //khi click vào sẽ mở button  
                }
            }
        }
        if(turn == true && checkMove(arr) == true){
            Move p = bestMove(arr, turn);
            int i = p.getX();
            int j = p.getY();
            arBT[i][j].setText("O");
            arBT[i][j].setForeground(Color.blue);
            arrEnd[i][j] = true;
            arr[i][j] = 2;
            turn = false;
            
            Move move = new Move(i, j);
            arrUndo[undo] = move;
            undo++;
        }
        int check = CheckWin(arr);
        if(check == 10 && ketthucgame == false) {
            may++;
            diem();
            JOptionPane.showConfirmDialog(null, "Máy thắng mất rồi !!", "Thông báo", JOptionPane.DEFAULT_OPTION);
            end();
            ketthucgame = true;
        } else if (check == -10 && ketthucgame == false) {
            nguoi++;
            diem();
            JOptionPane.showConfirmDialog(null, "Bạn thắng", "Thông báo", JOptionPane.DEFAULT_OPTION);
            end();
            ketthucgame = true;
        }
        if(ketthucgame == false){
            if(checkMove(arr) == false){
                hoa++;
                diem();
               JOptionPane.showConfirmDialog(null, "Hòa", "Thông báo", JOptionPane.DEFAULT_OPTION); 
                ketthucgame = true;
            }
        }
    }
    
    public void end(){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arrEnd[i][j] = true;
            }
        }
    }
       
    public static void main(String[] args) {
        new CaroMinimax();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
