package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.Visibility;

/**
 * Created by MSC on 02/04/2015.
 */
public class Workspace {
    User onwer;
    String name;
    double quote;
    Boolean mounted;
    List<User> accessList;
    Visibility visibility;
    List<String> tags;
}
