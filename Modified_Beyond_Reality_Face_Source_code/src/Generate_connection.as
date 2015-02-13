package {
	import flash.display.Sprite;
	import flash.net.Socket;
	
	public class Generate_connection extends Sprite {
		//private var socket:CustomSocket;
		public var sock:Socket
		private var response:String;
		
		public var info:String;
		public function  Generate_connection() {
			
			sock= new Socket();
			
		}
	
		public function sendRequest(data:String):void {
			
			sock.writeUTFBytes(data+="\n");
			sock.flush();
		}
		
	}
}
	