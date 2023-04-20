/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RoundRobinLoadBalancer extends LoadBalancer {

    private int counter = 0;
    private final ReentrantLock lock;

    public RoundRobinLoadBalancer(List <String> list) {
        super(list);
        lock = new ReentrantLock();
    }








