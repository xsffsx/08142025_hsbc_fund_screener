package example

class Example {
    static void main(String[] args) {
        // Using a simple println statement to print output to the console
        def x = 5
        println('Hello World ' + x);

        // GET
        def get = new URL("https://httpbin.org/get").openConnection();
        def getRC = get.getResponseCode();
        println(getRC);
        if(getRC.equals(200)) {
            println(get.getInputStream().getText());
        }

        // POST
        def post = new URL("https://httpbin.org/post").openConnection();
        def message = '{"message":"this is a message"}'
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.getOutputStream().write(message.getBytes("UTF-8"));
        def postRC = post.getResponseCode();
        println(postRC);
        if(postRC.equals(200)) {
            println(post.getInputStream().getText());
        }

        def html = "http://localhost:8080/graphiql".toURL().text
        println(html)

        String postResult
        ((HttpURLConnection)new URL('http://mytestsite/bloop').openConnection()).with({
            requestMethod = 'POST'
            doOutput = true
            setRequestProperty('Content-Type', '...') // Set your content type.
            outputStream.withPrintWriter({printWriter ->
                printWriter.write('...') // Your post data. Could also use withWriter() if you don't want to write a String.
            })
            // Can check 'responseCode' here if you like.
            postResult = inputStream.text // Using 'inputStream.text' because 'content' will throw an exception when empty.
        })
    }
}

