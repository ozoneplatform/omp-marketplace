package ozone.utils

class Post {

String url

URLConnection connection

String postText(def uuids)
{
	def thisUrl = new URL(url)
	
	connection = thisUrl.openConnection()
	connection.setRequestMethod("POST")
	connection.setRequestProperty("Content-Type","text/plain")
	connection.doOutput = true
	Writer writer = new OutputStreamWriter(connection.outputStream)
	
	def uuidSet = Utils.extractUuids(uuids)
	
	uuidSet.each{
		writer.write(it)
		writer.write(System.getProperty("line.separator"))
	}
	
	writer.flush()
	writer.close()

	connection.connect()
	 
	
	return connection.content.text
	
}

String postXhtml(def xhtml)
{
	def thisUrl = new URL(url)
	
	connection = thisUrl.openConnection()
	connection.setRequestMethod("POST")
	connection.setRequestProperty("Content-Type","application/xhtml+xml")
	connection.doOutput = true
	Writer writer = new OutputStreamWriter(connection.outputStream)

	writer.write(xhtml);
	
	
	writer.flush()
	writer.close()

	connection.connect()
	 
	
	return connection.content.text
	
}

String toString()
{
	return url + "?" + queryString.toString()
}

}

