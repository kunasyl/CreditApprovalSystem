
import java.util.Scanner;

abstract class User {

    public static final long MIN_ENTITY_INCOME = 3_000_000;
    public static final long MIN_PERSON_INCOME = 720_000;
    public static final long MAX_PHYSICAL_PERSON_CREDIT_AMOUNT = 3_000_000;
    public static final long MAX_ENTITY_CREDIT_AMOUNT = 30_000_000;

    public int workExperience;
    public long income;
    public boolean hasOverdueCredit;

    User(int workExperience, long income, boolean hasOverdueCredit) {
        this.workExperience = workExperience;
        this.income = income;
        this.hasOverdueCredit = hasOverdueCredit;
    }

    public abstract boolean checkData();

    public long countAnnualIncome(long monthIncome) {        // считает годовой доход
        return monthIncome*12;
    }
}

class Person extends User {     // физ. лицо
    private int age;
    private boolean isMan;

    Person(int age, boolean isMan, int workExperience, long income, boolean hasOverdueCredit) {
        super(workExperience,income,hasOverdueCredit);
        this.age = age;
        this.isMan = isMan;
    }

    @Override
    public boolean checkData() {    // проверяет данные физ. лица
        if (age>=18 && workExperience>=2 && countAnnualIncome(income)>= MIN_PERSON_INCOME && !hasOverdueCredit) {
            return (isMan && age < 63) || (!isMan && age < 58);
        }
        else return false;
    }

    public void getCreditApprovalData(boolean isCashForm, long collateral, long creditAmount) {     // возвращает информацию об одобрении кредита
        System.out.println("Вам одобрено.");
        if (isCashForm) {
            System.out.println("Кредит может быть выдан наличными.");
        }
        else {
            System.out.println("Кредит может быть выдан безналичными.");
        }
        if (creditAmount>MAX_PHYSICAL_PERSON_CREDIT_AMOUNT+collateral) {
            creditAmount = MAX_PHYSICAL_PERSON_CREDIT_AMOUNT+collateral;
        }
        System.out.println("Максимальная сумма кредита составляет: "+creditAmount);
    }
}

class Entity extends User {     // юр. лицо

    Entity(int workExperience, long income, boolean hasOverdueCredit) {
        super(workExperience,income,hasOverdueCredit);
    }

    @Override
    public boolean checkData() {    // проверяет данные юр. лица
        return workExperience >= 5 && countAnnualIncome(income) >= MIN_ENTITY_INCOME && !hasOverdueCredit;
    }

    public void getCreditApprovalData(long collateral, long creditAmount) {     // возвращает информацию об одобрении кредита
        System.out.println("Вам одобрено.");
        System.out.println("Кредит может быть выдан безналичными.");
        if (creditAmount>MAX_ENTITY_CREDIT_AMOUNT+collateral) {
            creditAmount = MAX_ENTITY_CREDIT_AMOUNT+collateral;
        }
        System.out.println("Максимальная сумма кредита составляет: "+creditAmount);
    }
}

public class Main {
    public static void main(String[] args) {
        boolean isPerson;

        Scanner scan = new Scanner(System.in);
        System.out.print("Вы физ. лицо? [Да/Нет] ");
        isPerson = scan.nextLine().equals("Да");

        System.out.print("Каков ваш стаж работы? ");
        int workExperience = scan.nextInt();

        System.out.print("Введите ваш ежемесечный доход: ");
        long monthIncome = scan.nextLong();

        System.out.print("Введите количество просроченных кредитов: ");
        boolean hasOverdueCredit = scan.nextInt() > 0;

        System.out.print("Введите стоимость залогового имущества при его наличии: ");
        long collateral = scan.nextLong();

        System.out.print("Введите запрашиваемую сумму: ");
        long creditAmount = scan.nextLong();

        if (isPerson) {
            System.out.print("Введите ваш возраст: ");
            int age = scan.nextInt();

            scan.nextLine();
            System.out.print("Введите ваш пол [Ж/М]: ");
            boolean isMan = scan.nextLine().equals("М");

            System.out.print("Введите форму выдачи кредита [Нал/Безнал]: ");
            boolean isCashForm = scan.nextLine().equals("Нал");

            Person person = new Person(age, isMan, workExperience, monthIncome, hasOverdueCredit);
            if (person.checkData()) {
                person.getCreditApprovalData(isCashForm, collateral, creditAmount);
            }
            else {
                System.out.println("Кредит не может быть выдан.");
            }
        }
        else {
            Entity entity = new Entity(workExperience, monthIncome, hasOverdueCredit);
            if (entity.checkData()) {
                entity.getCreditApprovalData(collateral, creditAmount);
            }
            else {
                System.out.println("Кредит не может быть выдан.");
            }
        }
    }
}