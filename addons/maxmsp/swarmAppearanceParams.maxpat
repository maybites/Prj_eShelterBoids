{
	"patcher" : 	{
		"fileversion" : 1,
		"rect" : [ 119.0, 133.0, 707.0, 525.0 ],
		"bglocked" : 0,
		"defrect" : [ 119.0, 133.0, 707.0, 525.0 ],
		"openrect" : [ 0.0, 0.0, 0.0, 0.0 ],
		"openinpresentation" : 1,
		"default_fontsize" : 12.0,
		"default_fontface" : 0,
		"default_fontname" : "Arial",
		"gridonopen" : 0,
		"gridsize" : [ 15.0, 15.0 ],
		"gridsnaponopen" : 0,
		"toolbarvisible" : 1,
		"boxanimatetime" : 200,
		"imprint" : 0,
		"enablehscroll" : 1,
		"enablevscroll" : 1,
		"devicewidth" : 0.0,
		"boxes" : [ 			{
				"box" : 				{
					"maxclass" : "number",
					"varname" : "incubationSize",
					"fontname" : "Arial",
					"minimum" : 0,
					"presentation_rect" : [ 2.0, 130.0, 48.0, 18.0 ],
					"maximum" : 100,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 295.0, 103.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-2",
					"outlettype" : [ "int", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "loadbang",
					"fontname" : "Arial",
					"numinlets" : 1,
					"fontsize" : 12.0,
					"numoutlets" : 1,
					"patching_rect" : [ 84.0, 177.0, 60.0, 20.0 ],
					"id" : "obj-1",
					"outlettype" : [ "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "- 1",
					"fontname" : "Arial",
					"numinlets" : 2,
					"fontsize" : 12.0,
					"numoutlets" : 1,
					"patching_rect" : [ 15.0, 39.0, 32.5, 20.0 ],
					"id" : "obj-12",
					"outlettype" : [ "int" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "inlet",
					"hint" : "switch",
					"numinlets" : 0,
					"numoutlets" : 1,
					"patching_rect" : [ 518.0, 27.0, 25.0, 25.0 ],
					"id" : "obj-14",
					"outlettype" : [ "" ],
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"varname" : "u772002275",
					"text" : "autopattr",
					"fontname" : "Arial",
					"numinlets" : 1,
					"fontsize" : 12.0,
					"numoutlets" : 4,
					"patching_rect" : [ 431.0, 203.0, 59.5, 20.0 ],
					"id" : "obj-13",
					"outlettype" : [ "", "", "", "" ],
					"restore" : 					{
						"blue" : [ 0.0 ],
						"green" : [ 0.0 ],
						"incubationSize" : [ 0 ],
						"red" : [ 0.0 ],
						"size" : [ 0.0 ]
					}

				}

			}
, 			{
				"box" : 				{
					"maxclass" : "inlet",
					"hint" : "bang",
					"numinlets" : 0,
					"numoutlets" : 1,
					"patching_rect" : [ 15.0, 7.0, 25.0, 25.0 ],
					"id" : "obj-11",
					"outlettype" : [ "" ],
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "gswitch2",
					"int" : 1,
					"presentation_rect" : [ 6.0, 160.0, 39.0, 32.0 ],
					"numinlets" : 2,
					"numoutlets" : 2,
					"patching_rect" : [ 35.0, 290.0, 39.0, 32.0 ],
					"presentation" : 1,
					"id" : "obj-9",
					"outlettype" : [ "", "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "swarm ID",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 54.0, 40.0, 53.0, 16.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 130.0, 208.0, 53.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-8"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "swarm appearance params",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 6.0, 4.0, 133.0, 16.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 294.0, 314.0, 132.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-7"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "panel",
					"presentation_rect" : [ 4.0, 3.0, 140.0, 16.0 ],
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 134.0, 294.0, 128.0, 128.0 ],
					"presentation" : 1,
					"id" : "obj-6",
					"bgcolor" : [ 0.933333, 0.709804, 0.031373, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 55.0, 339.0, 25.0, 25.0 ],
					"id" : "obj-5",
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "+ 1",
					"fontname" : "Arial",
					"numinlets" : 2,
					"fontsize" : 12.0,
					"numoutlets" : 1,
					"patching_rect" : [ 15.0, 88.0, 32.5, 20.0 ],
					"id" : "obj-4",
					"outlettype" : [ "int" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "umenu",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 1.0, 22.0, 100.0, 16.0 ],
					"items" : [ "Simulation", 1, ",", "Simulation", 2, ",", "Simulation", 3 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"types" : [  ],
					"numoutlets" : 3,
					"patching_rect" : [ 15.0, 64.0, 100.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-3",
					"outlettype" : [ "int", "", "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "number",
					"fontname" : "Arial",
					"numinlets" : 1,
					"fontsize" : 12.0,
					"numoutlets" : 2,
					"patching_rect" : [ 15.0, 113.0, 50.0, 20.0 ],
					"id" : "obj-78",
					"outlettype" : [ "int", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "sprintf /simulation%i/manager/boid/appearance",
					"fontname" : "Arial",
					"numinlets" : 1,
					"fontsize" : 12.0,
					"numoutlets" : 1,
					"patching_rect" : [ 15.0, 137.0, 261.0, 20.0 ],
					"id" : "obj-76",
					"outlettype" : [ "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "message",
					"text" : "#1",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 3.0, 41.0, 30.0, 14.0 ],
					"numinlets" : 2,
					"fontsize" : 10.0,
					"numoutlets" : 1,
					"patching_rect" : [ 92.0, 209.0, 30.0, 14.0 ],
					"presentation" : 1,
					"id" : "obj-74",
					"outlettype" : [ "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "incubation size",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 54.0, 130.0, 77.0, 16.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 347.0, 103.0, 95.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-68"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "blue",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 54.0, 112.0, 29.0, 16.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 347.0, 81.0, 29.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-66"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "blue",
					"bordercolor" : [ 0.0, 0.098039, 1.0, 1.0 ],
					"fontname" : "Helvetica",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 112.0, 47.0, 16.0 ],
					"maximum" : 1.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 295.0, 81.0, 47.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-67",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "green",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 54.0, 94.0, 36.0, 16.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 347.0, 59.0, 36.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-65"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "red",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 54.0, 76.0, 25.0, 16.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 347.0, 37.0, 25.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-56"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "size",
					"fontname" : "Helvetica",
					"presentation_rect" : [ 54.0, 58.0, 28.0, 16.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 347.0, 15.0, 28.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-45"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "size",
					"fontname" : "Helvetica",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 58.0, 47.0, 16.0 ],
					"maximum" : 200.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 295.0, 15.0, 47.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-44",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "green",
					"bordercolor" : [ 0.05098, 1.0, 0.0, 1.0 ],
					"fontname" : "Helvetica",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 94.0, 47.0, 16.0 ],
					"maximum" : 1.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 295.0, 59.0, 47.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-58",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "red",
					"bordercolor" : [ 1.0, 0.0, 0.0, 1.0 ],
					"fontname" : "Helvetica",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 76.0, 47.0, 16.0 ],
					"maximum" : 1.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 295.0, 37.0, 47.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-57",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "pak /simulation1/manager/boid/appearance 0 100. 4. 0.1 2. 3",
					"fontname" : "Arial",
					"numinlets" : 7,
					"fontsize" : 12.0,
					"numoutlets" : 1,
					"patching_rect" : [ 55.0, 250.0, 334.0, 20.0 ],
					"id" : "obj-55",
					"outlettype" : [ "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "panel",
					"presentation_rect" : [ 37.0, 152.0, 5.0, 48.0 ],
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 62.0, 266.0, 5.0, 79.0 ],
					"presentation" : 1,
					"id" : "obj-10",
					"bgcolor" : [ 0.0, 0.0, 0.0, 1.0 ]
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"source" : [ "obj-1", 0 ],
					"destination" : [ "obj-74", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-78", 0 ],
					"destination" : [ "obj-76", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-3", 0 ],
					"destination" : [ "obj-4", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-4", 0 ],
					"destination" : [ "obj-78", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-9", 1 ],
					"destination" : [ "obj-5", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-14", 0 ],
					"destination" : [ "obj-9", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-12", 0 ],
					"destination" : [ "obj-3", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-11", 0 ],
					"destination" : [ "obj-12", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-2", 0 ],
					"destination" : [ "obj-55", 6 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-55", 0 ],
					"destination" : [ "obj-9", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-74", 0 ],
					"destination" : [ "obj-55", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-67", 0 ],
					"destination" : [ "obj-55", 5 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-44", 0 ],
					"destination" : [ "obj-55", 2 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-57", 0 ],
					"destination" : [ "obj-55", 3 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-58", 0 ],
					"destination" : [ "obj-55", 4 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-76", 0 ],
					"destination" : [ "obj-55", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
 ]
	}

}
