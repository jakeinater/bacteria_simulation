package utils;
import java.io.*;

/*
 *  MIT License
 *  
 *  Copyright (c) 2003-2016 - Philip Isenhour
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE. 
 */

public class PipedDeepCopy {

	private static final Object ERROR = new Object();
	
	public static Object copy(Object orig) {
		Object obj = null;
		try {
			PipedInputStream in = new PipedInputStream();
			PipedOutputStream pos = new PipedOutputStream(in);
			
			Deserializer des = new Deserializer(in);
			
			ObjectOutputStream out = new ObjectOutputStream(pos);
			out.writeObject(orig);
			
			obj = des.getDeserializedObject();
			
			if (obj == ERROR)
				obj = null;
			}
		catch(IOException ioe) {
			ioe.printStackTrace();
			}
		
		return obj;
		}
	
	//helper class for cloning
	private static class Deserializer extends Thread {
		private Object obj = null;
		private Object lock = null;
		private PipedInputStream in = null;
		
		public Deserializer(PipedInputStream pin) throws IOException {
			lock = new Object();
			this.in = pin;
			start();
			}
		
		public void run() {
			Object o = null;
			try {
				ObjectInputStream oin = new ObjectInputStream(in);
				o = oin.readObject();
				}
			catch(IOException e) {
				e.printStackTrace();
				}
			catch(ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
				}
			
			synchronized(lock) {
				if (o == null)
					obj = ERROR;
				else
					obj = o;
				lock.notifyAll();
				}
			}
		
		public Object getDeserializedObject() {
			try {
				synchronized(lock) {
					while (obj == null) {
						lock.wait();
						}
					}
				}
			catch(InterruptedException ie) {
			}
			return obj;
		}	
	}
}

