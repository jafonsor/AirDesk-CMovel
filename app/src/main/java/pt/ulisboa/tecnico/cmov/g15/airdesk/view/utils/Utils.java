package pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSC on 08/04/2015.
 */
public class Utils {

    public static List<String> retrieveTagsFromInputText(String input){
        ArrayList<String> tagList = new ArrayList<String>();
        if (input!=null && !input.equals("")) {
            String[] split = input.split(";");
            for (String s : split) tagList.add(s.trim());
        }else{
            return null;
        }
        return tagList;
    }

    public static String generateStringTagsFromList(List<String>list){
        String tags = "";
        for(int i=0; i<list.size(); i++){
            tags+=list.get(i);
            if( (i+1) != list.size()) tags+=";";
        }
        return tags;
    }
}
