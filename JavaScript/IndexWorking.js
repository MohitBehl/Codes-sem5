console.log("inside js");
operatorList = ["add","subtract","multiply","divide","negation","and","or","modulo"];
operators = ["/","-","&","|","+","*","~","%"];

function mapping(key)
{
    console.log("recieved!!!!:"+key+typeof(key));
    switch(key){
        case "0": return 0;break;
        case "1": return 1;break;
        case "2": return 2;break;
        case "3": return 3;break;
        case "4": return 4;break;
        case "5": return 5;break;
        case "6": return 6;break;
        case "7": return 7;break;
        case "8": return 8;break;
        case "9": return 9;break;
        case "A": return 10;break;
        case "B": return 11;break;
        case "C": return 12;break;
        case "D": return 13;break;
        case "E": return 14;break;
        case "F": return 15;break;
        case "add": return "+";break;
        case "multiply": return "*";break;
        case "subtract": return "-";break;
        case "divide": return "/";break;
        case "modulo": return "%";break;
        case "and": return "&";break;
        case "or": return "|";break;
        case "negation": return "~";break;
    }
}

function reverseMap(key){
    console.log("recieved!!!!:"+key+typeof(key));
    switch(key){
        case "0": return "0";break;
        case "1": return "1";break;
        case "2": return "2";break;
        case "3": return "3";break;
        case "4": return "4";break;
        case "5": return "5";break;
        case "6": return "6";break;
        case "7": return "7";break;
        case "8": return "8";break;
        case "9": return "9";break;
        case "10": return "A";break;
        case "11": return "B";break;
        case "12": return "C";break;
        case "13": return "D";break;
        case "14": return "E";break;
        case "15": return "F";break;
    }
}

function keyboard(key){
    if(key == "AC"){
    refresh();
    return;
    }

    if(key == "equals"){
        if(number != null){
            total.push(number);
            number = null;
        }

        result = evaluate(); //evaluation
        changeDisplay(result);

        total.push("=");
        console.log(total);
        refresh();
        total.push(""+result);
        appendDisplay(result);
        return;
    }
    else if(isOperator(key)){
        //operator pressed
        if(number != null){
            total.push(number);
            number = null;
        }
        appendDisplay(mapping(key));
        total.push(mapping(key));
        console.log(total);
        return;
    }
    else{
        //numeral pressed
        if(number == null)
            number = ""+key;
        else
        number += key;
    }

    console.log("pressed:"+key);
    appendDisplay(key);
}

function refresh(){
    document.getElementById("resultText").innerHTML = "";
    expression = "";
    number = null;
    total = new Array();
    console.log("state refreshed");    
}
refresh();//first time initialization

function changeDisplay(str){
    document.getElementById("resultText").innerHTML = str;
}

function appendDisplay(str){
    value = document.getElementById("resultText").innerHTML;
    value += str;
    document.getElementById("resultText").innerHTML = value;
}


function isOperator(key){
    //console.log("in");
    for(i=0;i<operatorList.length;i++){
        if(operatorList[i] == key){
        console.log("operator pressed:"+key);
        return true;
        }
    }
    return false;
}

function isOper(str){
    for(i = 0;i<operators.length;i++){
        if(str == operators[i])
            return true;
    }
    return false;
}

function evaluate(){
    console.log(total);
    var str = "";
    for(var i = 0;i<total.length;i++){
        console.log(total[i]);
        if(!isOper(total[i])){
            val = total[i];
            str += convertToHexDecimal(val);            
        }
        else
        str += total[i];
    }
    var result = eval(str);
    console.log("expression in decimal:"+str);
    console.log("result in decimal:"+result);
    console.log("result in hexadecimal:"+result.toString("16"));
    return result.toString("16").toUpperCase();
}

function convertToHexDecimal(hexNum){
    hexNum = ""+hexNum;
    console.log("recieved:"+hexNum);
    decNum = 0;
    arr = hexNum.split('');
    var index = 0;
    for(var i=arr.length-1;i>=0;i--,index++){
        decNum += mapping(arr[i])*Math.pow(16,index);
    }
    console.log("hello"+decNum);
    return decNum;
}