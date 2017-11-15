package com.csc.flame.selenium.keywords;

 

import java.util.Map;

 

import org.openqa.selenium.Keys;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.support.ui.Select;

import org.springframework.stereotype.Service;

import com.csc.flame.selenium.HtmlObject;

import com.csc.flame.selenium.KeywordResult;

import com.csc.fsg.flame.model.common.ComponentModel;

 

import org.openqa.selenium.WebDriver;

import org.springframework.stereotype.Service;

import com.csc.flame.selenium.HtmlObject;

import com.csc.flame.selenium.KeywordResult;

import com.csc.fsg.flame.model.common.ComponentModel;

 

 

/**

Keyword Id = 'Sample'

Please see FLAME Java Docs at

http://flame-javadocs.s3-website-us-east-1.amazonaws.com

to know more about the referenced classes

**/

 

@Service("KeyPress")
public class KeyPressKeywordHandler implements KeywordHandler {

 

    public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {

        KeywordResult result = new KeywordResult();

       

        //read data from testData map using Component model params

        //String data = testData.get(componentModel.getParam1())

       

        //Set result property of KeywordResult object

        //result.setResult(true);

       

        //Set message property of KewyWordResult object

        //result.setMessage("Sample keyword executed successfully");

        Actions action = new Actions(driver);

        String datasheetVar1 = testData.get(componentModel.getParam1()).toString();


        try{  if (datasheetVar1 == null || datasheetVar1.equals(""))
            {
            result.setResult(true);
            result.setMessage("Key Press Skipped");
            }
        else{

                                    //tab to close dropdown if its in open state

                                    if(datasheetVar1.equalsIgnoreCase("F4"))
                                    {

                                    action.sendKeys(Keys.F4);
									action.perform();

                                    result.setResult(true);

                                                result.setMessage("F4 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F1")){

                                                action.sendKeys(Keys.F1);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F1 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F2")){

                                                action.sendKeys(Keys.F2);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F2 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F3")){

                                                action.sendKeys(Keys.F3);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F3 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F5")){

                                                action.sendKeys(Keys.F5);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F5 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F6")){

                                                action.sendKeys(Keys.F6);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F6 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F7")){

                                                action.sendKeys(Keys.F7);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F7 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F8")){

                                                action.sendKeys(Keys.F8);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F8 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F9")){

                                                action.sendKeys(Keys.F9);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F9 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F10")){

                                                action.sendKeys(Keys.F10);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F10 Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F11")){

                                                action.sendKeys(Keys.F11);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F11 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("F12")){

                                                action.sendKeys(Keys.F12);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("F12 Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("ENTER")){

                                                action.sendKeys(Keys.ENTER);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("Enter Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("TAB")){

                                        action.sendKeys(Keys.TAB);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Tab Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("DELETE")){

                                        action.sendKeys(Keys.DELETE);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Delete Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("ARROW_DOWN")){

                                        action.sendKeys(Keys.ARROW_DOWN);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Down Arrow Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("ARROW_UP")){

                                        action.sendKeys(Keys.ARROW_UP);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Up Arrow Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("ARROW_LEFT")){

                                        action.sendKeys(Keys.ARROW_LEFT);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Left Arrow Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("ARROW_RIGHT")){

                                        action.sendKeys(Keys.ARROW_RIGHT);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Right Arrow Key Pressed Succeffully");
                                    }else if(datasheetVar1.equalsIgnoreCase("PAGE_UP")){

                                        action.sendKeys(Keys.PAGE_UP);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Page Up Key Pressed Succeffully");

                                    }
                                    
                                    else if(datasheetVar1.equalsIgnoreCase("PAGE_DOWN")){

                                                action.sendKeys(Keys.PAGE_DOWN);
												action.perform();

                                                result.setResult(true);

                                                result.setMessage("Page Down Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("HOME")){

                                        action.sendKeys(Keys.HOME);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("Home Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("END")){

                                        action.sendKeys(Keys.END);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("End Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("BACKSPACE")){

                                        action.sendKeys(Keys.BACK_SPACE);
										action.perform();

                                        result.setResult(true);

                                        result.setMessage("BackSpace Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("DELETE")){

                                        action.sendKeys(Keys.DELETE);
										action.perform();
                                        result.setResult(true);
                                        result.setMessage("Delete Key Pressed Succeffully");

                                    }else if(datasheetVar1.equalsIgnoreCase("ESCAPE")){

                                        action.sendKeys(Keys.ESCAPE);
										action.perform();
                                        result.setResult(true);
                                        result.setMessage("Escape Key Pressed Succeffully");

                                    }else{

                                                result.setResult(false);

                                                result.setMessage("Key Not Recognized");

                                    }

                                               

                                    //click then set value in dropdown then press tab

                                }
                        }

        catch(Exception e9)

        {

            result.setResult(false);

            result.setMessage("Exception occured. " + e9.getMessage() + e9.getStackTrace()  );

        }

        return result;

    }

 

}
