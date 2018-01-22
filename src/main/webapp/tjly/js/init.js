var o = {
	init: function(){
		this.diagram();
	},
	random: function(l, u){
		return Math.floor((Math.random()*(u-l+1))+l);
	},
	diagram: function(){
		var r = Raphael('diagram', 600, 600),
			rad = 73,
			defaultText = '经济数据分析',
			speed = 250;
		
		r.circle(300, 300, 85).attr({ stroke: 'none', fill: '#193340' });
		
		var title = r.text(300, 300, defaultText).attr({
			font: '20px Arial',
			fill: '#fff'
		}).toFront();
		
		r.customAttributes.arc = function(value, color, rad){
			var v = 3.6*value,
				alpha = v == 360 ? 359.99 : v,
				random = o.random(91, 240),
				a = (random-alpha) * Math.PI/180,
				b = random * Math.PI/180,
				sx = 300 + rad * Math.cos(b),
				sy = 300 - rad * Math.sin(b),
				x = 300 + rad * Math.cos(a),
				y = 300 - rad * Math.sin(a),
				path = [['M', sx, sy], ['A', rad, rad, 0, +(alpha > 180), 1, x, y]];
			return { path: path, stroke: color }
		}
		
		var arcvalue = 30;
		$('.get').find('.arc').each(function(i){
			var t = $(this), 
				color = t.find('.color').val(),
				value = t.find('.percent').val(),
				text = t.find('.text').text();
			
			rad += 30;	
			arcvalue += 5;
			//while(arcvalue > 100)
				//arcvalue = arcvalue / 100;
		
			var z = r.path().attr({ arc: [arcvalue, color, rad], 'stroke-width': 26 });
			
			z.mouseover(function(){
                this.animate({ 'stroke-width': 50, opacity: .75 }, 1000, 'elastic');
                if(Raphael.type != 'VML') //solves IE problem
				this.toFront();
				title.stop().animate({ opacity: 0 }, speed, '>', function(){
					if(i == 0)
						this.attr({ text: text + '\n' + value + '平方米' }).animate({ opacity: 1 }, speed, '<');
					else{
						if(value > 10000)
							this.attr({ text: text + '\n' + (value / 10000).toFixed(2) + '万元' }).animate({ opacity: 1 }, speed, '<');
						else
							this.attr({ text: text + '\n' + value + '元' }).animate({ opacity: 1 }, speed, '<');
					}
				});
            }).mouseout(function(){
				this.stop().animate({ 'stroke-width': 26, opacity: 1 }, speed*4, 'elastic');
				title.stop().animate({ opacity: 0 }, speed, '>', function(){
					title.attr({ text: defaultText }).animate({ opacity: 1 }, speed, '<');
				});	
            });
		});
		
	}
}
$(function(){ o.init(); });
