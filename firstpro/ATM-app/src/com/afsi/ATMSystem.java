package com.afsi;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
*ATM入口
* */
public class ATMSystem {
    public static void main(String[] args) {
        ArrayList<Account> accounts = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        //首页
        while (true) {
            System.out.println("===========ATM==========");
            System.out.println("1、账户登录");
            System.out.println("2、账户开户");

            System.out.println("请您选择操作：");
            int command =  sc.nextInt();

            switch(command){
                case 1:
                    //登录
                    login(accounts , sc);
                    break;
                case 2:
                    //开户(atl + enter)
                    register(accounts,sc);
                    break;
                default:
                    System.out.println("命令不存在！");
            }
        }


    }

    /**
     * 登录功能
     * @param accounts
     * @param sc
     */
    private static void login(ArrayList<Account> accounts,Scanner sc) {
        System.out.println("=========登录操作==========");

        //1、判断是否存在账户
        if(accounts.size() == 0){
            System.out.println("对不起，您系统没有账户！请先开户。");
            return;//卫语言风格，解决方法的执行
        }

        while (true) {
            //2、进入登录
            System.out.println("请您输入登录卡号：");
            String cardId = sc.next();

            //3、判断卡号是否存在
            Account acc = getAccountByCardId(cardId , accounts);
            if(acc != null){
                while (true) {
                    //卡号存在
                    //4、输入密码，认证密码
                    System.out.println("请您输入登录密码：");
                    String passWord = sc.next();
                    //判断当前账户对象的密码是否和用户输入一
                    if(acc.getPassWord().equals(passWord)){
                        System.out.println("恭喜您，" + acc.getUsername() + "先生|女生进入系统，您的卡号是：" + acc.getCardId());
                        //.... 查询、转账、取款 ....
                        //展示登录后操作页
                        showUserCommand(sc , acc , accounts);
                        return;//干掉登录方法
                    }else{
                        System.out.println("对不起，您输入的密码有误！");
                    }
                }
            } else{
                System.out.println("对不起，系统没有这卡号！");
            }
        }

    }

    /**
     * 展示登录后界面
     */
    private static void showUserCommand( Scanner sc , Account acc , ArrayList<Account> accounts) {
        while (true) {
            System.out.println("======用户操作页=======");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、修改密码");
            System.out.println("6、退出");
            System.out.println("7、注销账户");
            System.out.println("请选择：");
            int  command = sc.nextInt();
            switch(command){
                case 1:
                    //查询账户(当前账户)
                    showAccount(acc);
                    break;
                case 2:
                    //存款
                    depositMoney(acc , sc);
                    break;
                case 3:
                    //取款
                    drawMoney(acc,sc);
                    break;
                case 4:
                    //转账
                    transferMoney(sc , acc , accounts);
                    break;
                case 5:
                    //修改密码
                    updatePassWord(sc, acc);
                    return;//干掉操作页方法，回到首页
                case 6:
                    //退出
                    System.out.println("退出成功");
                    return;//干掉当前方法执行，跳出去
                case 7:
                    //注销账户
                    if (deleteAccount(acc,sc,accounts)){
                        //销户成功回到首页
                        return;
                    }else{
                        //没有销户成功，返回操作首页
                        break;
                    }
                default:
                    System.out.println("输入有误！");
            }
        }
    }

    /**
     * 销户功能
     * @param acc
     * @param sc
     * @param accounts
     */
    private static boolean deleteAccount(Account acc, Scanner sc, ArrayList<Account> accounts) {
        //从当前账户集合中，删除当前账户对象
        System.out.println("=========用户销户操作========");
        System.out.println("您真的要销户?y/n");
        String rs = sc.next();
        switch(rs){
            case "y":
                if(acc.getMoney() > 0){
                    System.out.println("您账户还有钱，不可销户~");
                }else {
                    accounts.remove(acc);
                    System.out.println("您的账户销户完成~~");
                    return true;
                }
                break;
            default:
                System.out.println("当前账户继续保留！");
        }
        return false;
    }

    /**
     * 修改密码功能
     * @param sc
     * @param acc
     */
    private static void updatePassWord(Scanner sc, Account acc) {
        System.out.println("===========用户密码修改操作==========");
        while (true) {
            System.out.println("请您输入当前密码：");
            String passWord = sc.next();

            if(acc.getPassWord().equals(passWord)){
                while (true) {
                    System.out.println("请您输入新密码：");
                    String newPassWord = sc.next();

                    System.out.println("请您确认新密码：");
                    String okPassWord = sc.next();
                    if(newPassWord.equals(okPassWord)){
                        acc.setPassWord(newPassWord);
                        System.out.println("恭喜您，密码修改成功！");
                        return;
                    }else{
                        System.out.println("您输入2次密码不一致");
                    }
                }
            }else{
                System.out.println("您输入密码不正确~");
            }
        }
    }

    /**
     * 转账功能
     * @param sc
     * @param acc 当前账户对象
     * @param accounts 全部账户的集合
     */
    private static void transferMoney(Scanner sc, Account acc, ArrayList<Account> accounts) {
        System.out.println("===========用户转账操作==========");
        //1、是否有2个以上账户
        if(accounts.size() < 2){
            System.out.println("当前系统不足2个账户！请开户！");
            return;
        }

        //2、判断是否有钱
        if(acc.getMoney() == 0){
            System.out.println("对不起，您的账户没钱！");
            return;
        }

        while (true) {
            //3、开始转账
            System.out.println("请您输入对方账户卡号：");
            String cardId = sc.next();

            //判断是否是自己账户
            if(cardId.equals(acc.getCardId())){
                System.out.println("对不起，您不能给自己转账~~");
                continue;
            }

            //判断卡号是否存在
            //对方账户
            Account account = getAccountByCardId(cardId , accounts);
            if(account == null){
                System.out.println("对不起，您输入的这个账户不存在~~");
            }else{
                //账户存在
                //认证姓氏
                String userName = account.getUsername();
                String tip = "*" + userName.substring(1);
                System.out.println("请对方输入["+ tip +"]的姓氏" );
                String preName = sc.next();

                if(userName.startsWith(preName)){
                    while (true) {
                        //认证通过
                        System.out.println("请您输入转账金额：");
                        double money = sc.nextDouble();
                        //判断余额是否够
                        if(money > acc.getMoney()){
                            System.out.println("对不起，您余额不足，最多可以转账：" + acc.getMoney());
                        }else{
                            //余额足够
                            acc.setMoney(acc.getMoney() - money);
                            account.setMoney(account.getMoney() + money);
                            System.out.println("转账成功！您的余额还剩余：" + acc.getMoney());
                            return;//干掉转账方法
                        }
                    }
                }else{
                    System.out.println("对不起您输入的信息有误！");
                }
            }
        }
    }

    /**
     *  取款
     * @param acc 当前账户对象
     * @param sc
     */
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("===========用户取钱操作==========");
        if(acc.getMoney() < 100){
            System.out.println("不足100元，无法取钱！");
            return;
        }

        while (true) {
            //取款功能
            System.out.println("请您输入取款金额：");
            double money = sc.nextDouble();

            if(money > acc.getQuotaMoney()){
                System.out.println("对不起，您当前取款金额超过限额，每次最多可取：" + acc.getQuotaMoney());
            }else{
                //判断是否超过账户余额
                if(money > acc.getMoney()){
                    System.out.println("余额不足！账户目前余额是：" + acc.getMoney());
                }else{
                    //可以取款
                    System.out.println("恭喜取款：" + money + "元，成功！");
                    //更新余额
                    acc.setMoney(acc.getMoney() - money);
                    //取款结束
                    showAccount(acc);
                    return;
                }
            }
        }
    }

    /**
     * 存钱
     * @param acc 当前账户对象
     * @param sc
     */
    private static void depositMoney(Account acc, Scanner sc) {
        System.out.println("=========用户存钱操作==========");
        System.out.println("请输入您存款金额：");
        double money = sc.nextDouble();

        //更新金额
        acc.setMoney(acc.getMoney() + money);
        System.out.println("恭喜您存款成功！");
        showAccount(acc);
    }

    /**
     * 展示账户信息
     * @param acc
     */
    private static void showAccount(Account acc) {
        //当前账户信息
        System.out.println("============当前账户信息============");
        System.out.println("卡号：" + acc.getCardId());
        System.out.println("户主：" + acc.getUsername());
        System.out.println("余额：" + acc.getMoney());
        System.out.println("限额：" + acc.getQuotaMoney());
    }

    /**
     * 用户开户功能
     * @param accounts 接受的账户集合
     */
    private static void register(ArrayList<Account> accounts,Scanner sc) {
        System.out.println("============系统开户操作==========");
        Account account = new Account();

        System.out.println("请您输入账户用户名: ");
        String userName = sc.next();
        account.setUsername(userName);

        while (true) {
            System.out.println("请您输入账户密码: ");
            String passWord = sc.next();
            System.out.println("请您输入确认密码: ");
            String okpassWord = sc.next();
            if(okpassWord.equals(passWord)){
                account.setPassWord(okpassWord);
                break;
            }else{
                System.out.println("对不起，您输入的两次密码不一致，请重新输入~~");
            }
        }

        //设置定额
        System.out.println("请您输入账户当次限额: ");
        double quotaMoney = sc.nextDouble();
        account.setQuotaMoney(quotaMoney);

        //产生随机8位卡号
        String cardId = getRandomCardId(accounts);
        account.setCardId(cardId);

        accounts.add(account);
        System.out.println("恭喜您，" + userName + "先生/女生，您开户成功，您的卡号是：" + cardId + "，请您务必记住");
    }

    /**
     * 生成8位账户卡号不能重复
     * @return
     */
    private static String getRandomCardId(ArrayList<Account> accounts) {
        Random r = new Random();
        while (true) {
            String cardId = "";
            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }

            //独立一个方法判断卡号是都重复
            Account acc = getAccountByCardId(cardId , accounts);
            //查询不到账户cardId
            if (acc == null){
                //无重复卡号
                return cardId;
            }

            //查询到就是重复，接着重新生成
        }
    }

    /**
     * 根据卡号查询一个账户对象出来
     * @param cardId 卡号
     * @param accounts 账户集合
     * @return 账户对象 | null
     */
    private static Account getAccountByCardId(String cardId , ArrayList<Account> accounts){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null;
    }


}
