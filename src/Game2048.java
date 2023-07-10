import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game2048 extends JFrame {
    private int[][] data=new int[4][4] ;        //定义数组数据，二维数组4行4列用于渲染数据
    private Boolean isOver=false;               //判断游戏是否继续
    private int score=0;                        //游戏的分数
    public Game2048(){
        initFrame();                            //初始化窗口
      //  initPanle();
        initData(2);                             //初始化数据需要几个方块就生成几个
        paintView();                            //渲染界面函数
        setKeyListener();                       //设置事件监听
    }
    //初始化数据
    public void initData(int num){
        for (int i=0;i< data.length;i++){
            for (int j=0;j<data[i].length;j++){
                data[i][j]=0;
            }
        }
        for (int i=0;i<num;i++){
            RandomDataXY();
        }
    }
    //重新开始游戏
    public void restart(){
        if(isOver){
            isOver=false;
            initData(1);
            paintView();
        }
    }
    public void setKeyListener(){
        //添加事件监听
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case 37:moveLeft();click();          //按下鼠标左键执行左移动方法
                    break;
                    case 39:moveRight();click();        //按下鼠标右键执行右移动方法
                    break;
                    case 38:moveUp();click();            //按下鼠标上键执行上移动方法
                    break;
                    case 40:moveDown();click();          //按下鼠标下键执行下移动方法
                    break;
                    case 32:restart();click();
                    break;

                }
            }
        });
    }
    public void click(){
	RandomDataXY();
        check();
        paintView();
    }
    public  void  initFrame(){
        setTitle("2048");                                 //设置标题
        setSize(514,538);                    //设置窗口大小
        setLocation(100,100);                       //设置距离窗口位置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //设置可以退出
        setResizable(false);                              //设置窗口大小不可变
        setLayout(null);                                  //设置布局模式为空
    }
    //渲染函数
    public void paintView(){
        getContentPane().removeAll();                                                     //移出所有组件
        if(isOver){     //判断是否结束
            JLabel failView=new JLabel(new ImageIcon("image//B-lose.png"));
            failView.setBounds(90,100,334,228);
            getContentPane().add(failView);
        }
        //渲染图片界面
        for (int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                JLabel jlabel=new JLabel(new ImageIcon("image//B-"+data[i][j]+".png"));
                jlabel.setBounds(50+100*j,50+100*i,100,100);
                getContentPane().add(jlabel);
            }
        }
        //渲染背景图片
        JLabel background=new JLabel(new ImageIcon("image//B-Background.jpg"));
        background.setBounds(40,40,420,420);
        getContentPane().add(background);
        //渲染得分界面
        JLabel scoreLabel = new JLabel("得分: " + score);
        scoreLabel.setBounds(50, 20, 100, 20);
        getContentPane().add(scoreLabel);
        getContentPane().repaint();   //重新渲染
    }
    //数组左合并
    public void moveLeft(){
        for (int k=0;k<4;k++){
            int[] newArray=new int[4];
            int index=0;
            for (int j=0;j<4;j++){
                if(data[k][j]!=0){
                    newArray[index]=data[k][j];
                    index++;
                }
            }
            for (int i=0;i<3;i++){
                if(newArray[i]==newArray[i+1]){
                    newArray[i]=newArray[i]*2;
                    score+=newArray[i];
                    for (int j=i+1;j<3;j++){
                        newArray[j]=newArray[j+1];
                    }
                    newArray[3]=0;
                }
            }
            data[k]=newArray;
        }
    }
    public void test(int[][] src){
        for (int[] v:src){
            for (int i:v){
                System.out.print(i+" ");
            }
            System.out.println();
        }
    }
    public void moveRight(){
        reverseArray();
        moveLeft();
        reverseArray();
    }
    public  void moveUp(){
        anticlockwise();
        moveLeft();
        Clockwise();
    }
    public void moveDown(){
       Clockwise();
        moveLeft();
        anticlockwise();
    }
    public void anticlockwise(){
        int[][] newArray=new int[4][4];
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                newArray[3-j][i]=data[i][j];
            }
        }
        data=newArray;
    }
    public void  Clockwise(){
        int[][] newArray=new int[4][4];
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                newArray[j][3-i]=data[i][j];
            }
        }
        data=newArray;
    }
    public void reverseArray(){
        for (int i=0;i<4;i++){
            for (int j=0;j<2;j++){
                int temp=data[i][j];
                data[i][j]=data[i][3-j];
                data[i][3-j]=temp;
            }
        }
    }
    //随机生成
    public void RandomDataXY(){
        int[] arrayX=new int[16];      //存储x坐标
        int[] arrayY=new int[16];      //存储y坐标
        int[] valueArray={2,2,2,2,4};  //随机生成数字的值
        int length=0;                  //剩余块的多少，随机数根据这个数进行生成
        for (int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++) {
                if (data[i][j] == 0) {
                    arrayX[length] = i;
                    arrayY[length] = j;
                    length++;
                }
            }
        }
        if(length!=0){                //判断是否空块位置是否为0，如果为0进行生成。
            Random r=new Random();
            int index=r.nextInt(length);
            int valueIndex=r.nextInt(5);
            data[arrayX[index]][arrayY[index]]=valueArray[valueIndex];
        }
    }
    //复制数组
    public void copyArray(int[][] src,int[][] dec){
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                src[i][j]=dec[i][j];
            }
        }
    }
    //检查是否你上下左右移动,如果都不能移动游戏就结束
    public void check(){
        if(checkFun("left")&&checkFun("right")&&checkFun("up")&&checkFun("down")){
            isOver=true;
        }
    }
    //判断两个数组是否相等
    public Boolean equalArray(int[][]src,int[][] dec){
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                if(src[i][j]!=dec[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    public Boolean checkFun(String s){
        int[][] copyArray=new int[4][4];
        copyArray(copyArray,data);
        switch (s){
            case "left":moveLeft();
            break;
            case "right":moveRight();
            break;
            case "up":moveUp();
            break;
            case "down":moveDown();
            break;
        }
        if(!equalArray(data,copyArray)){     //判断两个数组是否相等，如果相等代表不能合并
            data=copyArray;
            return false;
        }
        return true;
    }
    public static void main(String[] args) {
        new Game2048().setVisible(true);
    }
}
