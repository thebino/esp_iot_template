/**
 *   Electrical enclosure - Body v0.1
 *   
 *   Copyright © 2022 Stürmer Benjamin
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

// number of fragments
$fn = 50;


// corner size in millimeters
corner_radius = 1;

module body(
    width, 
    depth, 
    height, 
    thickness
) {
    difference() {
        /**
         * bottom part with rounded corners and flat top
         */
        difference() {
            // 1st part: body with rounded corners
            translate([
                corner_radius, 
                corner_radius, 
                corner_radius
            ]) {
                color("orange")
                minkowski() {
                    cube([
                        width - corner_radius * 2, 
                        depth - corner_radius * 2, 
                        height - corner_radius * 2 + thickness
                    ], center = false);
                    sphere(corner_radius);
                }
            }
            
            // 2nd part: flat cut off on top
            translate([0, 0, height]) {
                color("red")
                cube([
                    width, 
                    depth, 
                    thickness
                ]);
            }
        }
        
        /**
         * negative hollow inside of the enclosure
         */
        translate([
                thickness + corner_radius,
                thickness + corner_radius, 
                thickness + corner_radius
            ]) {
            minkowski() {
                color("green")
                cube([
                    width - corner_radius * 2 - thickness * 2,
                    depth - corner_radius * 2 - thickness * 2,
                    height - thickness * 2 + thickness
                ], center = false);
                sphere(corner_radius);
            }
        }

    
        /**
         * ledge to fit the top enclosure into
         */
        translate([
            corner_radius + thickness / 1.5,
            corner_radius + thickness / 1.5,
            height - thickness
        ]) {
            color("cyan")
            linear_extrude(thickness * 2) {
                minkowski() {   
                    square([
                        width - thickness * 2 - corner_radius,
                        depth - thickness * 2 - corner_radius
                    ]);
                    circle(1);
                }
            }
        }
    }
}

body(20, 20, 10, 1.2);