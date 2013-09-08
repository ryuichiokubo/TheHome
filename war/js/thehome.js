$(function() {
	// get update and save in localStorage
	$.get('http://localhost:8888/thehome', function(data) {
		data.forEach(function(article) {
			var hash = article.hash;
			var data = JSON.stringify({
				title: article.title,
				summary: article.summary,
				link: article.link,
				time: article.time
			});
			if (!localStorage.getItem(hash)) {
				localStorage.setItem(hash, data);
			}
		});
	});

	// read from localStorage
	var item, obj;
	var arr = [];
	for( var i=0; i<localStorage.length; i++ ) {
		item = localStorage.getItem(localStorage.key(i));
		try {
			obj = JSON.parse(item);
		} catch(e) {
			console.log("parse error: " + e);
			// XXX next loop
			// XXX check undefined obj
		}
		obj.hash = localStorage.key(i);
		obj.time = obj.time ? parseInt(obj.time, 10) : Date.now();

		// remove hidden data which is old enough not to be in rss anymore
		var TOO_OLD = 3 * 24 * 60 * 60 * 1000; // 3 day
		if (obj.hide && obj.time < Date.now() - TOO_OLD ) {
			localStorage.removeItem(localStorage.key(i));
		}

		arr.push(obj);
	}	
	arr.sort(function(a, b){
		return b.time - a.time;
	});


	var prevId, position; 
	var LIMIT = 100;
	arr.forEach(function(o, i) {
		if( i < LIMIT && !o.hide ) {
			$("#contents").append('<div id="dialog' + o.hash + '" title="' + o.title + '">' + o.summary + "<br /><br />" + (new Date(o.time)).toLocaleString() + "</div>");

			if ( prevId ) {
				position = {
					my: 'center top+190',
					of: "#" + prevId,
					collision: 'fit none'
				};
			} else {
				position = {
					my: 'left+2% top+5%',
					at: 'left+2% top+5%',
					of: window,
					collision: 'fit none'
				};
			}

			$( "#dialog" + o.hash ).dialog({
				width: 900,
				position: position,
				resizable: false,
				draggable: false,
				buttons: [ { text: "Read more", click: function() { window.location = o.link; } } ],
				close: function(event, ui) {
					o.hide = true;
					localStorage.setItem(o.hash, JSON.stringify(o));
				}
			});
			prevId = "dialog" + o.hash;
		}
	});
});