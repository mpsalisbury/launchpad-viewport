# launchpad-viewport
***Animation library for controlling Launchpad devices***

[![Main Site][gh-pages-shield]][gh-pages-link]
[![Build Status][travis-shield]][travis-link]

## Overview

LaunchpadViewport is a library for interacting with [Launchpad
devices](https://global.novationmusic.com/launch/launchpad#).
Launchpad devices consist of a set of buttons that can act as
inputs (buttons you can push) and as outputs (light up with colors).
Launchpads are designed to act as MIDI inputs and outputs, and
as such are used mostly with music-making software. However,
the device can be considered as a general input/output device.
We weren't able to find a library that made it easy to flexibly
render animations on the Launchpad buttons, so we decided to
build one ourselves.  Please let us know what you think and if
you have any ideas for new capabilities we should add.

## Components

### Viewport

A _Viewport_ is the core mechanism used to interact with Launchpad devices. 
Control is separated into two parts:
* Layers are used to control the button lights.
* Listeners are used to respond to button presses.

Layers consist of two separate interfaces.

A WriteLayer allows you to set the pixel values within the Layer.
And a ReadLayer allows you to read the pixel values of the Layer.
A Viewport consists of a stacked set of Layers. Each Layer consists of
a rectangular grid of Pixels.  Each Pixel within a Layer consists of 
a Color (red, green, blue) and an alpha (transparency). The overall color
of a pixel within a Viewport is determined by combining the colors and
alphas of the pixels within each layer. 

To draw into a Viewport, you must first add a Layer to it.

Simple example:

```java
// Add a new layer to the viewport.
WriteLayer layer = viewport.addLayer();
// Set one pixel to be blue.
layer.setPixel(1, 2, Color.BLUE);
```
 
### Coordinates

Viewports and Layers represent two-dimensional grids of buttons.
The extent of each is represented by a Range2 object which describes
a 2-dimensional range of x- and y-values. Low values represent the
lower-left corner of the grid, with coordinate values increasing up 
and to the right.

### WriteLayer

### ReadLayer
### PixelListener
### CloseListener

ButtonListener will let you respond to button presses.

SubViewports

Devices

Layout

Animation
FramedAnimation
DecayingAnimation

Applications


<!-- references -->

[gh-pages-shield]:
https://img.shields.io/badge/main%20site-mpsalisbury.github.io/launchpad--viewport-ff55ff.png?style=flat
[gh-pages-link]: http://mpsalisbury.github.io/launchpad-viewport/
[travis-shield]: https://img.shields.io/travis/mpsalisbury/launchpad-viewport.png
[travis-link]: https://travis-ci.org/mpsalisbury/launchpad-viewport

