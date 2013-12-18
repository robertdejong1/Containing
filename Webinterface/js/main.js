// Load the Visualization API and the piechart package.
google.load('visualization', '1.0', {'packages':['corechart']});
           
function drawChart(stats) {
    // Create the data table.
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Topping');
    data.addColumn('number', 'Slices');
    data.addRows([
        ['Barge', 3],
        ['Seaship', 1],
        ['Train', 1],
        ['Truck', 1],
        ['AGV', 2],
        ["Storage", 5]
    ]);

    // Set chart options
    var options = {'title':'Amount of containers', 'width':500, 'height':400};

    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.BarChart(document.getElementById('stats'));
    
    chart.draw(data, options);
    $("#stats div").css("position", "static");
}