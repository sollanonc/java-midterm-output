package midterm;

import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Midterm
    {
    public static void main(String[] args)
     {
        ListofPrsn lp = new ListofPrsn();
        FMngt fm = new FMngt("data.csv", lp);
        WinMngt wm = new WinMngt(lp, fm);

        wm.showMenu();
    }
}

class Prsn
    {
    String name;
    int age;
    
    public Prsn(String name, int age) 
    {
        this.name = name;
        this.age = age;
    }
}

class ListofPrsn
    {
    public ArrayList<Prsn> prsnArrayList = new ArrayList<>();
    
    public void addEntry(String name, int age)     //Add Entry
            
    {
        Prsn newPrsn = new Prsn(name, age);
        prsnArrayList.add(newPrsn);
    }
    
    public void deleteEntry(int index)       //Delete Entry
    {
        prsnArrayList.remove(index);
    }
    
    public void updateEntry(String name, int age, int index)      //Update An Entry
    {
        prsnArrayList.get(index).name = name;
        prsnArrayList.get(index).age = age;
    }
}

final class FMngt
    {
    String filePath = "data.txt";
    ListofPrsn lp;
    
    public FMngt (String filePath, ListofPrsn lp) 
    { 
        this.lp = lp;
        this.filePath = filePath;
        CreateFile();    
        ReadFile();
    }
    
    public void CreateFile()
    {
        try 
        {
            File myFile = new File(filePath);
            if (myFile.createNewFile()) 
            {
                System.out.println("File created: " + myFile.getName());
            } else 
            {
                System.out.println("File already exists.");
            }
        } catch (IOException e) 
        {
            System.out.println("An error occured.");
        }
    }

    public void ReadFile () 
        { 
        try 
        {
            File myFile = new File(filePath);
            try (Scanner myReader = new Scanner(myFile) //Converts Text File into PersonArrayList Format
            ) {
                while (myReader.hasNextLine())
                {
                    String line = myReader.nextLine();
                    String[] lineArray = line.split(",");
                    Prsn newPrsn = new Prsn(lineArray[0], Integer.parseInt(lineArray[1]));
                    lp.prsnArrayList.add(newPrsn);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An Error Occurred.");
        }
    }

    public void WriteToFile() 
    {
        try 
        {
            try (FileWriter myWriter = new FileWriter(filePath)) {
                for (Prsn p : lp.prsnArrayList)
                {
                    myWriter.write(p.name+","+p.age+"\n");
                }
            }
            System.out.println("         * * Success!! * *" + "\n" + "You Have Put Some Changes to the File.\n" + "       * * * * * * * * * * *" + "\n");
        } catch (IOException e) 
        {
            System.out.println("An Error Occurred.");
        }
    }
}

class WinMngt  
    {
    ListofPrsn lp;
    FMngt fm;
    public WinMngt(ListofPrsn lp, FMngt fm) 
    {
        this.lp = lp;
        this.fm = fm;
    }

    public void showMenu() 
                {
        String[] options = {"Add Entry", "Delete Entry", "View All Entries", "Update An Entry", "Exit"};
        int choice = JOptionPane.showOptionDialog(null,
                "                                                              ======OPTIONS======",
                "java-midterm-output", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice)
            {
            case 0:
                showAdd();
                
                break;
            case 1:
                showDelete();
                
                break;
            case 2:
                showEntries();
                
                break;
            case 3:
                showUpdate();
                
                break;
            case 4:
                showExit();
                
                break;
            }
    }

    public void showAdd() 
    {
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        Object[] addField = 
        {
                "              = ADD ENTRY =" + 
                "                      \n\n",
                "                    Name: ",
                name,
                "                      Age: ",
                    age };
        
        int confirm = JOptionPane.showConfirmDialog(null, addField, "Add Entry", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION)  
            {
            try 
            {
                int ageInt = Integer.parseInt(age.getText());
                lp.addEntry(name.getText(), ageInt);
                fm.WriteToFile();
                String message = "You have Successfully Added " + name.getText();
                showSuccess(message);
            } 
            catch (NumberFormatException e) 
            {
                showAdd();
            }
        } 
        else 
        {
            showMenu();
        }
    }

    public void showDelete() 
        {
        JTextField deleteIndex = new JTextField();
        String entriesField = "";
        for (int i = 0; i < lp.prsnArrayList.size(); i++) 
        {
            entriesField += i+1 + ". " + lp.prsnArrayList.get(i).name + " is " + lp.prsnArrayList.get(i).age + " years old." + "\n";
        }
        entriesField += "\n";

        Object[] deleteField = 
        {
                entriesField,
                "             = DELETE ENTRY =" + 
                "                      \n\n",
                "Please Select Number to Delete: ",
                deleteIndex
        };
        
        int confirm = JOptionPane.showConfirmDialog(null, deleteField, "Delete Entry", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            try {
                int deleteIndexInt = Integer.parseInt(deleteIndex.getText()) - 1;
                if (deleteIndexInt >= lp.prsnArrayList.size()) {
                    throw new IndexOutOfBoundsException("Index " + deleteIndexInt + " is out of bounds!");
                }
                String message = "You have Successfully Deleted " + lp.prsnArrayList.get(deleteIndexInt).name + ". ";
                lp.deleteEntry(deleteIndexInt);
                fm.WriteToFile();
                
                showSuccess(message);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                showDelete();
            }
        } else 
        {
            showMenu();
        }
    }

    public void showEntries() 
        {
        String[] entriesButton = {"Back To Menu"};
        String entriesField = "";

        for (int i = 0; i < lp.prsnArrayList.size(); i++) 
        {
            entriesField += i+1 + ". " + lp.prsnArrayList.get(i).name + " is " + lp.prsnArrayList.get(i).age + " years old.\n";
        }

        entriesField += "\n";

        JOptionPane.showOptionDialog(null, entriesField, "View All Entries", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, entriesButton, entriesButton[0]);
        showMenu();
    }

    public void showUpdate() 
        {
        JTextField updateIndex = new JTextField();
        String entriesField = "";
        for (int i = 0; i < lp.prsnArrayList.size(); i++) 
        {
            entriesField += i+1 + ". " + lp.prsnArrayList.get(i).name + " is " + lp.prsnArrayList.get(i).age + " years old." + "\n";
        }
        entriesField += "\n";

        Object[] updateField = 
                {
                entriesField,
                "              = UPDATE AN ENTRY = " + 
                "                      \n\n",
                "Select a Number to Update: ",
                updateIndex
        };
        int confirm = JOptionPane.showConfirmDialog(null, updateField, "Update An Entry", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) 
                {
            try 
                {
                int updateIndexInt = Integer.parseInt(updateIndex.getText()) - 1;
                if (updateIndexInt >= lp.prsnArrayList.size()) 
                {
                    throw new IndexOutOfBoundsException("Index " + updateIndexInt + " is out of bounds!");
                }
                showUpdatePrompt(updateIndexInt);
            } catch (NumberFormatException | IndexOutOfBoundsException e) 
            {
                showUpdate();
            }
        } 
        
        else
           {
            showMenu();
        }
    }
    public void showUpdatePrompt(int updateIndexInt)
        {
        JTextField newName = new JTextField();
        JTextField newAge = new JTextField();
        Object[] updateField = 
        {
                "Current Name: " + lp.prsnArrayList.get(updateIndexInt).name,
                "Current Age: " + lp.prsnArrayList.get(updateIndexInt).age + "\n\n",
                "                     New Name: ", newName,
                "                     New Age: ", newAge,
                "\n"
        };
        int confirm = JOptionPane.showConfirmDialog(null, updateField, "Updating " + lp.prsnArrayList.get(updateIndexInt).name, JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) 
            {
            try
            {
                int newAgeInt = Integer.parseInt(newAge.getText());
                String message = lp.prsnArrayList.get(updateIndexInt).name +
                 " is Successfully Updated to\n" + newName.getText() + " with the Age of " + newAge.getText() + ". " ;
                lp.updateEntry(newName.getText(), newAgeInt, updateIndexInt);
                fm.WriteToFile();
                showSuccess(message);
            } 
            catch (NumberFormatException e)
            {
                showUpdatePrompt(updateIndexInt);
            }
        } 
        else
        {
            showUpdate();
        }
    }
    
public void showSuccess(String message) 
    {
        String[] successField = {"Back To Menu"};
        JOptionPane.showOptionDialog(null, message, "Success!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, successField, successField[0]);
        showMenu();
    }

    private void showExit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}