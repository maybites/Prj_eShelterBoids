{
	"patcher" : 	{
		"fileversion" : 1,
		"rect" : [ 25.0, 69.0, 707.0, 525.0 ],
		"bglocked" : 0,
		"defrect" : [ 25.0, 69.0, 707.0, 525.0 ],
		"openrect" : [ 0.0, 0.0, 0.0, 0.0 ],
		"openinpresentation" : 1,
		"default_fontsize" : 10.0,
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
					"maxclass" : "newobj",
					"text" : "loadbang",
					"fontname" : "Arial",
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 1,
					"patching_rect" : [ 78.0, 173.0, 52.0, 18.0 ],
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
					"varname" : "u509002257",
					"text" : "autopattr",
					"fontname" : "Arial",
					"numinlets" : 1,
					"fontsize" : 12.0,
					"numoutlets" : 4,
					"patching_rect" : [ 431.0, 203.0, 59.5, 20.0 ],
					"id" : "obj-13",
					"outlettype" : [ "", "", "", "" ],
					"restore" : 					{
						"alignmentDamper" : [ 0.0 ],
						"coherenceDamper" : [ 0.0 ],
						"maxSpeed" : [ 0.0 ],
						"maxSteerForce" : [ 0.0 ],
						"neightborhoodRadius" : [ 0.0 ],
						"repulsionDamper" : [ 0.0 ],
						"repulsionRadius" : [ 0.0 ]
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
					"presentation_rect" : [ 6.0, 205.0, 39.0, 32.0 ],
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
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 43.0, 54.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 135.0, 211.0, 54.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-8"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "swarm physics params",
					"fontname" : "Arial",
					"presentation_rect" : [ 6.0, 4.0, 114.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 294.0, 314.0, 128.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-7"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "panel",
					"presentation_rect" : [ 4.0, 3.0, 144.0, 19.0 ],
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
					"fontname" : "Arial",
					"presentation_rect" : [ 1.0, 24.0, 100.0, 18.0 ],
					"items" : [ "Simulation", 1, ",", "Simulation", 2, ",", "Simulation", 3 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"types" : [  ],
					"numoutlets" : 3,
					"patching_rect" : [ 15.0, 64.0, 100.0, 18.0 ],
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
					"text" : "sprintf /simulation%i/manager/boid/physics",
					"fontname" : "Arial",
					"numinlets" : 1,
					"fontsize" : 12.0,
					"numoutlets" : 1,
					"patching_rect" : [ 15.0, 137.0, 237.0, 20.0 ],
					"id" : "obj-76",
					"outlettype" : [ "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "message",
					"text" : "#1",
					"fontname" : "Arial",
					"presentation_rect" : [ 6.0, 44.0, 30.0, 16.0 ],
					"numinlets" : 2,
					"fontsize" : 10.0,
					"numoutlets" : 1,
					"patching_rect" : [ 97.0, 212.0, 30.0, 16.0 ],
					"presentation" : 1,
					"id" : "obj-74",
					"outlettype" : [ "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "repulsion radius",
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 177.0, 82.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 325.0, 147.0, 82.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-72"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "repulsionRadius",
					"fontname" : "Arial",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 177.0, 48.0, 18.0 ],
					"maximum" : 100.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 273.0, 147.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-73",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "repulsion damper",
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 158.0, 89.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 325.0, 125.0, 89.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-70"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "repulsionDamper",
					"fontname" : "Arial",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 158.0, 48.0, 18.0 ],
					"maximum" : 5.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 273.0, 125.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-71",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "coherence damper",
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 139.0, 95.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 325.0, 103.0, 95.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-68"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "coherenceDamper",
					"fontname" : "Arial",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 139.0, 48.0, 18.0 ],
					"maximum" : 5.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 273.0, 103.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-69",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "alignement damper",
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 120.0, 97.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 325.0, 81.0, 97.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-66"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "alignmentDamper",
					"fontname" : "Arial",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 120.0, 48.0, 18.0 ],
					"maximum" : 5.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 273.0, 81.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-67",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "max steer force",
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 101.0, 80.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 325.0, 59.0, 80.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-65"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "max speed",
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 82.0, 60.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 325.0, 37.0, 60.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-56"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "neighborhood radius",
					"fontname" : "Arial",
					"presentation_rect" : [ 54.0, 63.0, 104.0, 18.0 ],
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 0,
					"patching_rect" : [ 325.0, 15.0, 104.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-45"
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "neightborhoodRadius",
					"fontname" : "Arial",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 63.0, 48.0, 18.0 ],
					"maximum" : 200.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 273.0, 15.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-44",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "maxSteerForce",
					"fontname" : "Arial",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 101.0, 48.0, 18.0 ],
					"maximum" : 2.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 273.0, 59.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-58",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"varname" : "maxSpeed",
					"fontname" : "Arial",
					"minimum" : 0.0,
					"presentation_rect" : [ 2.0, 82.0, 48.0, 18.0 ],
					"maximum" : 50.0,
					"numinlets" : 1,
					"fontsize" : 10.0,
					"numoutlets" : 2,
					"patching_rect" : [ 273.0, 37.0, 48.0, 18.0 ],
					"presentation" : 1,
					"id" : "obj-57",
					"outlettype" : [ "float", "bang" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "pak /simulation1/manager/boid/physics 0 100. 4. 0.1 2. 3. 1. 25.",
					"fontname" : "Arial",
					"numinlets" : 9,
					"fontsize" : 12.0,
					"numoutlets" : 1,
					"patching_rect" : [ 55.0, 250.0, 347.0, 20.0 ],
					"id" : "obj-55",
					"outlettype" : [ "" ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "panel",
					"presentation_rect" : [ 37.0, 197.0, 5.0, 48.0 ],
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
					"source" : [ "obj-11", 0 ],
					"destination" : [ "obj-12", 0 ],
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
					"source" : [ "obj-14", 0 ],
					"destination" : [ "obj-9", 0 ],
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
					"source" : [ "obj-55", 0 ],
					"destination" : [ "obj-9", 1 ],
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
					"source" : [ "obj-3", 0 ],
					"destination" : [ "obj-4", 0 ],
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
					"source" : [ "obj-76", 0 ],
					"destination" : [ "obj-55", 0 ],
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
					"source" : [ "obj-73", 0 ],
					"destination" : [ "obj-55", 8 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-71", 0 ],
					"destination" : [ "obj-55", 7 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-69", 0 ],
					"destination" : [ "obj-55", 6 ],
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
 ]
	}

}
