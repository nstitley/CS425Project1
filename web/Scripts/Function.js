var Function = ( function () {
    
    return {
        
        getData: function (sessionid) {
            
            var that = this;
            var code = $("select").val();
            
            $.ajax({
                
                url: 'registration?code=' + code,
                method: 'GET',
                dataType: 'html',
                success: function(response) {
                    that.output(response);                    
                }
                
            });
            
        },
        
        output: function(response) {
            
            $("#output").html(response);
            
        },
        
        addEntry: function (first, last, dpname, id) {
            
            var that = this;
            var first = $("#first").val();
            var last = $("#last").val();
            var dbname = $("#dpname").val();
            var code = $("select").val();
            
            $.ajax({
                
                url: 'registration?code=' + first + ";" + last + ";" + dbname + ";" + code,
                method: 'POST',
                dataType: 'json',
                success: function(response) {
                    that.outputJSON(response);
                }
                
            });
        },
        
        outputJSON: function(response) {
            
            var message = "Congratulations! You have successfully registered as: ";
            var message2 = "Your registration code is: ";
            $("#output").html("<p>" + message + response["displayname"] + "<br />" + message2 + response["code"] + "</p>");
            
        }
    };
}());


