package com.wdxxl.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class CheckZooKeeper {
	private static final int SESSION_TIMEOUT = 30000;
	ZooKeeper zooKeeper;
	Watcher watcher = new Watcher(){
		@Override
		public void process(WatchedEvent event) {
			System.out.println("event = "+event.toString());
		}
	};
	
	private void createZooKeeperInstance() throws IOException{
		zooKeeper = new ZooKeeper("127.0.0.1:2181", CheckZooKeeper.SESSION_TIMEOUT, this.watcher);
	}
	
	private void operateZooKeeper() throws KeeperException, InterruptedException{
		System.out.println("==创建 ZooKeeper 节点 (znode ： zoo2, 数据： myData2 ，权限： OPEN_ACL_UNSAFE ，节点类型： Persistent");
		zooKeeper.create("/zoo2", "myData2".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("查看是否创建成功： "+ new String(zooKeeper.getData("/zoo2", false, null)));
		System.out.println("==修改节点数据 ");
		zooKeeper.setData("/zoo2", "zhangsan".getBytes(), -1);
		System.out.println("查看是否修改成功： " + new String(zooKeeper.getData("/zoo2", false, null)));
		System.out.println("==删除节点 ");
		zooKeeper.delete("/zoo2", -1);
		System.out.println("查看节点是否被删除： 节点状态： [" + zooKeeper.exists("/zoo2", false) + "]");
	}
	
	private void closeZooKeeper() throws InterruptedException{
		zooKeeper.close();
	}
	
	public static void main(String[] args) throws Exception {
		CheckZooKeeper checkZooKeeper = new CheckZooKeeper();
		checkZooKeeper.createZooKeeperInstance();
		checkZooKeeper.operateZooKeeper();
		checkZooKeeper.closeZooKeeper();
	}
}
