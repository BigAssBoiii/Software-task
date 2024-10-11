/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.micode.notes.gtask.data;

import android.database.Cursor;
import android.util.Log;

import net.micode.notes.tool.GTaskStringUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class MetaData extends Task {
    private final static String TAG = MetaData.class.getSimpleName();

    private String mRelatedGid = null;

    public void setMeta(String gid, JSONObject metaInfo) {
        try {
            metaInfo.put(GTaskStringUtils.META_HEAD_GTASK_ID, gid);
        } catch (JSONException e) {
            Log.e(TAG, "failed to put related gid");
        }
        setNotes(metaInfo.toString());
        setName(GTaskStringUtils.META_NOTE_NAME);
    }

    public String getRelatedGid() {
        return mRelatedGid;
    }

    @Override
    public boolean isWorthSaving() {
        return getNotes() != null;
    }

    @Override
    public void setContentByRemoteJSON(JSONObject js) {
        super.setContentByRemoteJSON(js);
        if (getNotes() != null) {
            try {
                JSONObject metaInfo = new JSONObject(getNotes().trim());
                mRelatedGid = metaInfo.getString(GTaskStringUtils.META_HEAD_GTASK_ID);
            } catch (JSONException e) {
                Log.w(TAG, "failed to get related gid");
                mRelatedGid = null;
            }
        }
    }

    @Override
    public void setContentByLocalJSON(JSONObject js) {
        // this function should not be called
        throw new IllegalAccessError("MetaData:setContentByLocalJSON should not be called");
    }

    @Override
    public JSONObject getLocalJSONFromContent() {
        throw new IllegalAccessError("MetaData:getLocalJSONFromContent should not be called");
    }

    @Override
    public int getSyncAction(Cursor c) {
        throw new IllegalAccessError("MetaData:getSyncAction should not be called");
    }

}

    public static List<String> parseSuffixExpression(List<String> ls) {
        //符号栈
        Stack<String> s1 = new Stack<String>();
        //储存中间结果的List
        List<String> s2 = new ArrayList<String>();
        for (String item : ls) {
            if (item.matches("\\d+")) {
                s2.add(item);
            } else if (item.equals("(")) {
                s1.push(item);
            } else if (item.equals(")")) {
                //如果为有括号，则需要弹出s1中的运算符，直到遇到左括号
                while (!s1.peek().equals("(")) {
                    s2.add(s1.pop());
                }
                s1.pop();
            } else {
                while (s1.size() != 0 && getValue(s1.peek()) >= getValue(item)) {
                    s2.add(s1.pop());
                }
                s1.push(item);
            }
        }
        //将s1中剩余的运算符依次弹出并压入s2
        while (s1.size() != 0) {
            s2.add(s1.pop());
        }
        return s2;
    }

    private static int calculate(List<String> list) {
          Stack<Integer> stack = new Stack<>();
          for(int i=0; i<list.size(); i++){
              String item = list.get(i);
             if(item.matches("\\d+")){
                 //是数字
                 stack.push(Integer.parseInt(item));
             }else {
                 //是操作符，取出栈顶两个元素
                 int num2 = stack.pop();
                 int num1 = stack.pop();
                 int res = 0;
                 if(item.equals("+")){
                     res = num1 + num2;
                 }else if(item.equals("-")){
                     res = num1 - num2;
                 }else if(item.equals("*")){
                     res = num1 * num2;
                 }else if(item.equals("/")){
                     res = num1 / num2;
                 }else {
                     throw new RuntimeException("运算符错误！");
                 }
                 stack.push(res);
            }
         }
         return stack.pop();
    }
public static int getValue(String operation) {
        int result = 0;
        switch (operation) {
            case "+":
                result = 1;
                break;
            case "-":
                result = 1;
                break;
            case "*":
                result = 2;
                break;
            case "/":
                result = 2;
                break;
            default:
                System.out.println("不存在该运算符");
                break;
        }
        return result;
    }
