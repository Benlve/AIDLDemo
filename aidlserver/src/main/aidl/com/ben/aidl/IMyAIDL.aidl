package com.ben.aidl;

import com.ben.aidl.Person;

interface IMyAIDL {
    //传递基本数据类型
    String sendBasicData(int data, String comment);

    //传递Person
    Person modify(inout Person p);

}