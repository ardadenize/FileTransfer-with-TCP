/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import java.util.Collections;
import java.util.List;

public abstract class LoadBalancer {
    final List <String> ipList;

    public LoadBalancer(List <String> ipList) {
        this.ipList = Collections.unmodifiableList(ipList);
    }

    abstract String getIp();
}