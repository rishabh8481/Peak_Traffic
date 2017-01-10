import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
	static class node{
		ArrayList<node> interact;
		String name;
		public node(String name){
			interact = new ArrayList<node>();
			this.name=name;
		}
		public void addAdjacent(node n){
			this.interact.add(n);
		}
		public void removeAdjacent(int index){
			this.interact.remove(index);
		}
	}
	public static void main (String[] args) throws IOException{
		File file = new File(args[0]);
		@SuppressWarnings("resource")
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line; 
		String[] strArray=null;
		ArrayList<node> nodes=new ArrayList<node>();
		HashMap<String, Boolean> checkExist=new HashMap<String, Boolean>();
		HashMap<String, Integer> checkIndex=new HashMap<String, Integer>();
		while ((line = in.readLine()) != null){	
			strArray=line.split("\t");
			if(!checkExist.containsKey(strArray[1])){
				node newNode=new node(strArray[1]);
				nodes.add(newNode);
				checkExist.put(newNode.name, true);
				checkIndex.put(strArray[1], nodes.size()-1);
			}
			if(!checkExist.containsKey(strArray[2])){
				node newNode=new node(strArray[2]);
				nodes.add(newNode);
				checkExist.put(newNode.	name, true);
				checkIndex.put(strArray[2], nodes.size()-1);
			}
			for(node n:nodes){
				if(n.name.equals(strArray[2])&&!nodes.get(checkIndex.get(strArray[1])).interact.contains(n))
					nodes.get(checkIndex.get(strArray[1])).interact.add(n);
			}
		}
		int maxSize=Integer.MIN_VALUE;
		node max = null;
		for(node n:nodes){
			if(n.interact.size()>maxSize){
				maxSize=n.interact.size();
				max=n;
			}
		}
		ArrayList<node> rList=new ArrayList<node>();
		for(int i =0;i<nodes.size();i++)
			rList.add(nodes.get(i));
		for(node n:max.interact){
			for(int i=0;i<rList.size();i++){
				if(rList.get(i).name.equals(n.name))
					rList.remove(rList.get(i));
			}
		}
		ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
		ArrayList<String> currList=new ArrayList<String>();
		for(int i=0;i<rList.size();i++){
			currList.add(rList.get(i).name);
			makeCluster(currList,rList.get(i),nodes,result);
			currList.remove(rList.get(i).name);
		}
		String s="";
		ArrayList<String> sResult=new ArrayList<String> ();
		for(ArrayList<String> al:result){
			if(al.size()>=3){
				s="";
				for(int i=0;i<al.size();i++){
					if(i==al.size()-1)
						s+=al.get(i);
					else
						s+=al.get(i)+", ";
				}
				sResult.add(s);
			}
		}
		Collections.sort(sResult);
		for(int i=0;i<sResult.size();i++)
			System.out.println(sResult.get(i));
	}
	public static void makeCluster(ArrayList<String> currList,node currNode, ArrayList<node> pList, ArrayList<ArrayList<String>> result){
		ArrayList<node> newList=new ArrayList<node>();	
		for(node s:pList){
			if(currNode.interact.contains(s)&&!currList.contains(s.name))
				newList.add(s);
		}
		if(newList.isEmpty()){
			ArrayList<String> toAdd=new ArrayList<String>();
			for(int i=0;i<currList.size();i++)
				toAdd.add(currList.get(i));
			Collections.sort(toAdd);
			addList(result,toAdd);
		}
		else{
			for(int i=0;i<newList.size();i++){
				if(!currList.contains(newList.get(i).name)){
					currList.add(newList.get(i).name);
					makeCluster(currList,newList.get(i), currNode.interact, result);
					currList.remove(newList.get(i).name);
				}
			}
		}
	}
	public static void addList(ArrayList<ArrayList<String>> result,  ArrayList<String> addList){
		if(result.isEmpty())
			result.add(addList);
		else{
			boolean add=true;
			int replaceIndex=-1;
			for(int i=0;i<result.size();i++){
				if(result.get(i).size()>=addList.size()){
					if(includeArray(result.get(i),addList)){
						add=false;
						break;
					}
				}
				else{
					if(includeArray(addList,result.get(i))){
						replaceIndex=i;
						break;
					}
				}
			}
			if(add){
				if(replaceIndex>=0)
					result.set(replaceIndex, addList);
				else
					result.add(addList);
			}
		}
	}
	public static boolean includeArray(ArrayList<String> s1,ArrayList<String> s2){
		for(int i=0;i<s2.size();i++){
			if(!s1.contains(s2.get(i)))
				return false;
		}
		return true;
	}
	public static void matchNode(ArrayList<node> nodes, int checkNode, int currentNodePosition, int currentNode){
		if(!feasible(nodes,  checkNode, currentNodePosition, currentNode))
			nodes.get(checkNode).interact.remove(currentNodePosition);		
	}
	@SuppressWarnings("unused")
	public static boolean feasible(ArrayList<node> nodes, int checkNode, int currentNodePosition, int currentNode){
		for(int i =currentNodePosition+1;i<nodes.get(checkNode).interact.size();i++){
			return false;
		}			
		return true;
	}
}