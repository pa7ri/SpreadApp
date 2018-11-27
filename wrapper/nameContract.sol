pragma solidity ^0.4.0;

contract nameContract {

    string name;

    function setName(string n) public {

         name = n;

    }

    function getName() constant returns(string){

         return name;

    }

}
