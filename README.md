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
alphas of the pixels within each layer at that position. 

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

A _WriteLayer_ allows you to set the pixel values of its various
buttons via the four `setPixel()` methods. If you wish to perform an
animation, call `nextFrame()` before rendering your next frame's worth
of pixels. And when you are done using that layer, call `close()`
to remove it from its viewport.

### ReadLayer

Components that wish to be able to read the values of pixels in a layer
should use the _ReadLayer_ interface. In addition to the `getPixel()`
methods, it also allows you to listen for changes to any pixel in the
layer via `addPixelListener()`.

### ButtonListener

In order to respond to Viewport button presses, you should install a
_Button2Listener_ via `Viewport.addListener()`. It will be notified
when any button in the viewport is pressed or released.

### SubViewports

Some applications will prefer to split the given Viewport into parts
that have different responsibilities. To support that we provide
methods to generate a subview of the viewport.

```
// Returns a new viewport relative to this one.
Viewport getSubViewport(Viewport viewport, Range2 extent);
```

```
// Returns a new one-dimensional viewport relative to this viewport.
// extent must be one button wide or one button high.
Viewport1 getSubViewport1(Viewport viewport, Range2 extent);
```
This method returns a one-dimensional viewport (a _Viewport1_).
Buttons in it are indexed by int rather than Point. Also, it
does not support Layers -- it directly supports setting pixels
within its extent.

```
// Returns a new one-button viewport relative to this viewport.
Viewport0 getSubViewport0(Viewport viewport, Point p);
```
This method and the next return a 0-dimensional viewport
(a _Viewport0_, just one button).

```
// Returns a new one-button viewport relative to this viewstrip.
Viewport0 getSubViewport0(Viewport1 viewstrip, int x);
```

### FramedAnimation

A _FramedAnimation_ is a baseclass that supports rendering animations
one frame at a time. The subclass writes into a single layer (fetched
via `getWriteLayer()`, and being sure to call writeLayer.nextFrame()
before each frame) and registers any animation timelines via
`addTimeline()`. 

### DecayingAnimation
A _DecayingAnimation_ wraps a ReadLayer and allows it to render
individual frames, and then instead of erasing the frame upon the
call to `nextFrame()`, it decays the alpha gradually until it
becomes transparent. This acts like a decaying tail behind the
rendered frames.

### LaunchpadApplication

A _LaunchpadApplication_ is the standard base class for implementing
LaunchpadViewport applications. It uses [JavaFX][javafx-link] 
[Timelines][timeline-link] to execute the animations. Applications
implement `run()`, which should call `getViewport()` to fetch the
Viewport specified by the user. Given the Viewport, you can
then set up any button listeners and animations you need for
your application.

#### Devices

The LaunchpadApplication accepts an argument (`device`) to specify which
Launchpad devices you wish it to use populate the Viewport. The
current available device types are:

 * mk2 - Specifies all connected Launchpad Mk2 devices.
 * javafx - Specifies a JavaFX button grid, defaulting to an 8x8 grid.
   * You can modify the size of the javafx button grid by appending .{x}x{y}
   * You can request multiple copies of a window by appending the size with .{numCopies}
 * and you can request multiple types by separating them with commas
   * e.g.
   * javafx.8x8 requests a single 8x8 grid
   * javafx.20x10.3 requests three 20x10 grids
   * mk2,javafx.8x8.2,javafx.20x10 requests all mk2 devices, two 8x8 grids, and one 20x10 grid.

If you specify more than one device, you will need to specify how the
application should use them together. That is specified with the layout
described next.

#### Layout

Since a LaunchpadApplication works with only a single Viewport, when you
specify more than one device we provide a mechanism to combine them into
a single Viewport. This is the `layout` argument, and we currently support:
 * pickone - The user selects one of the specified devices by clicking a button on it. [default]
 * horiz - The devices will be arranged in a horizontal row. The user clicks on the devices in
   left-to-right order.
 * linked - The devices will be arranged in an arbitrary layout. The user specifies the layout by
   marking which layout is adjacent to which other layouts. Start by clicking on a button at the
   edge of a device that is adjacent to a button on another device. Then click on the adjacent
   button to link those two devices. Then continue clicking on pairs of adjacent buttons, linking
   the existing devices to a new device each time, until all devices have been included.
 * e.g.
   * --device=javafx.8x8.3 --layout=linked

<!-- references -->

[gh-pages-shield]: https://img.shields.io/badge/main%20site-mpsalisbury.github.io/launchpad--viewport-ff55ff.png?style=flat
[gh-pages-link]: http://mpsalisbury.github.io/launchpad-viewport/
[travis-shield]: https://img.shields.io/travis/mpsalisbury/launchpad-viewport.png
[travis-link]: https://travis-ci.org/mpsalisbury/launchpad-viewport
[javafx-link]: https://docs.oracle.com/javafx/
[timeline-link]: http://docs.oracle.com/javase/8/javafx/api/javafx/animation/Timeline.html
