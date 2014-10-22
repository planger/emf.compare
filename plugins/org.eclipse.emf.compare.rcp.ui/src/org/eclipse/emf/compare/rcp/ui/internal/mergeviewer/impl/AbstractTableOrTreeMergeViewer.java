/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import com.google.common.base.Objects;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;

/**
 * An abstract specialization of {@link AbstractStructuredMergeViewer} for Tables or Trees.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
public abstract class AbstractTableOrTreeMergeViewer extends AbstractStructuredMergeViewer {

	/** IMAGE_GAP. */
	static final int IMAGE_GAP = 5;

	/** DELTA_IMAGE_GAP. */
	static final int DELTA_IMAGE_GAP = 2;

	/** TEXT_GAP. */
	static final int TEXT_GAP = 2;

	/** CELL_GAP. */
	static final int CELL_GAP = 1;

	/** The color provider of this viewer. */
	private final ICompareColor.Provider fColorProvider;

	/** A listener that listen to erase items events. */
	private Listener fEraseItemListener;

	/** A listener that listen to paint items events. */
	private Listener fPaintItemListener;

	/** This will be used in order to resize the table items to an even height. */
	private MesureItemListener fMesureItemListener;

	/**
	 * Default constructor.
	 * 
	 * @param parent
	 *            the parent widget of this viewer.
	 * @param side
	 *            the side of this viewer.
	 * @param colorProvider
	 *            the color provider to use with this viewer.
	 * @param compareConfiguration
	 *            the compare configuration object to use with this viewer.
	 */
	public AbstractTableOrTreeMergeViewer(Composite parent, MergeViewerSide side,
			ICompareColor.Provider colorProvider, IEMFCompareConfiguration compareConfiguration) {
		super(parent, side, compareConfiguration);

		getStructuredViewer().setUseHashlookup(true);
		getStructuredViewer().setComparer(new ElementComparer());

		fColorProvider = colorProvider;
		fEraseItemListener = new Listener() {
			public void handleEvent(Event event) {
				AbstractTableOrTreeMergeViewer.this.handleEraseItemEvent(event);
			}
		};
		getStructuredViewer().getControl().addListener(SWT.EraseItem, fEraseItemListener);
		fPaintItemListener = new Listener() {
			public void handleEvent(Event event) {
				AbstractTableOrTreeMergeViewer.this.handlePaintItemEvent(event);
			}
		};
		getStructuredViewer().getControl().addListener(SWT.PaintItem, fPaintItemListener);

		fMesureItemListener = new MesureItemListener();
		getStructuredViewer().getControl().addListener(SWT.MeasureItem, fMesureItemListener);
	}

	/**
	 * Handle the paint item event.
	 * 
	 * @param event
	 *            the paint item event.
	 */
	protected void handlePaintItemEvent(Event event) {
		AbstractTableOrTreeItemWrapper itemWrapper = AbstractTableOrTreeItemWrapper.create((Item)event.item);
		String text = itemWrapper.getText(event.index);
		Image image = itemWrapper.getImage(event.index);
		/* center column 1 vertically */

		Point size = event.gc.textExtent(text);
		int yOffset = Math.max(0, (event.height - size.y) / 2);
		int xOffset = event.x;
		if (image != null) {
			int imageYOffset = Math.max(0, (event.height - image.getBounds().height) / 2);
			event.gc.drawImage(image, event.x + IMAGE_GAP, event.y + imageYOffset);
			xOffset += IMAGE_GAP + image.getBounds().width;
		}
		event.gc.drawText(text, xOffset + TEXT_GAP, event.y + yOffset, true);
		event.width += 2;
	}

	/**
	 * Handle the erase item event.
	 * 
	 * @param event
	 *            the erase item event.
	 */
	protected void handleEraseItemEvent(Event event) {
		// let the #handlePaintItem handle the painting of foreground
		event.detail &= ~SWT.FOREGROUND;

		Item item = (Item)event.item;
		AbstractTableOrTreeItemWrapper itemWrapper = AbstractTableOrTreeItemWrapper.create(item);
		Object data = itemWrapper.getData();

		if (data instanceof IMergeViewerItem) {
			IMergeViewerItem mergeViewerItem = ((IMergeViewerItem)data);
			Diff diff = mergeViewerItem.getDiff();
			if (diff != null) {
				if (MergeViewerUtil.isVisibleInMergeViewer(diff, getDifferenceGroupProvider(),
						getDifferenceFilter())
						&& !MergeViewerUtil.isMarkAsMerged(diff, mergeViewerItem, getCompareConfiguration())) {
					if (mergeViewerItem.isInsertionPoint()) {
						paintItemDiffBox(event, itemWrapper, diff, getBoundsForInsertionPoint(event,
								itemWrapper));
					} else {
						paintItemDiffBox(event, itemWrapper, diff, getBounds(event, itemWrapper));
					}
				}
			}
		}
	}

	/**
	 * Paint a box around the given diff, and a line that will be related to associated element in the
	 * opposite viewer.
	 * 
	 * @param event
	 *            the paint item event.
	 * @param itemWrapper
	 *            a TableItemWrapper or TreeItemWrapper.
	 * @param diff
	 *            the given Diff.
	 * @param bounds
	 *            a Rectangle that contains coordinates of the box to paint.
	 */
	private void paintItemDiffBox(Event event, AbstractTableOrTreeItemWrapper itemWrapper, Diff diff,
			Rectangle bounds) {
		event.detail &= ~SWT.HOT;

		GC g = event.gc;
		Color oldBackground = g.getBackground();
		Color oldForeground = g.getForeground();

		setGCStyleForDiff(g, diff, isSelected(event));
		g.fillRectangle(bounds);
		g.drawRectangle(bounds);

		if (diff.getKind() == DifferenceKind.MOVE) {
			g.setLineStyle(SWT.LINE_SOLID);
		}

		switch (getSide()) {
			case LEFT:
				drawLineFromBoxToCenter(itemWrapper, bounds, g);
				break;
			case RIGHT:
				drawLineFromCenterToBox(itemWrapper, bounds, g);
				break;
			default:
				break;
		}

		if (isSelected(event)) {
			g.setForeground(event.display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
			g.setBackground(event.display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			event.detail &= ~SWT.SELECTED;
		} else {
			g.setBackground(oldBackground);
			g.setForeground(oldForeground);
		}
	}

	/**
	 * Draw a line from center to box.
	 * 
	 * @param itemWrapper
	 *            a TableItemWrapper or TreeItemWrapper.
	 * @param boxBounds
	 *            a Rectangle that contains coordinates of the box.
	 * @param g
	 *            the SWT GC tool.
	 */
	private void drawLineFromCenterToBox(AbstractTableOrTreeItemWrapper itemWrapper, Rectangle boxBounds, GC g) {
		AbstractTableOrTreeItemWrapper parent = itemWrapper.getParentItem();
		final int xOffset;
		if (getContentProvider() instanceof ITreeContentProvider) {
			final boolean hasChildren = ((ITreeContentProvider)getContentProvider()).hasChildren(itemWrapper
					.getData());
			if (hasChildren) {
				if (parent != null) {
					xOffset = parent.getImageBounds(0).x;
				} else {
					xOffset = 0;
				}
			} else {
				xOffset = boxBounds.x;
			}
		} else {
			xOffset = boxBounds.x;
		}

		Rectangle itemBounds = itemWrapper.getBounds();
		Point from = new Point(0, 0);
		from.y = itemBounds.y + (itemBounds.height / 2);
		Point to = new Point(xOffset, from.y);
		g.drawLine(from.x, from.y, to.x, to.y);
	}

	/**
	 * Draw a line from box to center.
	 * 
	 * @param itemWrapper
	 *            a TableItemWrapper or TreeItemWrapper.
	 * @param boxBounds
	 *            a Rectangle that contains coordinates of the box.
	 * @param g
	 *            the SWT GC tool.
	 */
	private void drawLineFromBoxToCenter(AbstractTableOrTreeItemWrapper itemWrapper, Rectangle boxBounds, GC g) {
		Rectangle itemBounds = itemWrapper.getBounds();
		Rectangle clientArea = itemWrapper.getParent().getClientArea();
		Point from = new Point(0, 0);
		from.x = boxBounds.x + boxBounds.width;
		from.y = itemBounds.y + (itemBounds.height / 2);
		Point to = new Point(0, from.y);
		to.x = clientArea.x + clientArea.width;
		g.drawLine(from.x, from.y, to.x, to.y);
	}

	/**
	 * Computes the bounds (as Rectangle) in case of the given Item is an insertion point.
	 * 
	 * @param event
	 *            the paint item event.
	 * @param itemWrapper
	 *            a TableItemWrapper or TreeItemWrapper.
	 * @return the bounds (as Rectangle) of the insertion point.
	 */
	private static Rectangle getBoundsForInsertionPoint(Event event,
			AbstractTableOrTreeItemWrapper itemWrapper) {
		Rectangle fill = getBounds(event, itemWrapper);
		Rectangle treeBounds = itemWrapper.getParent().getClientArea();
		Rectangle itemBounds = itemWrapper.getBounds();
		fill.x = itemWrapper.getImageBounds(0).x + 2;
		fill.y = fill.y + (itemBounds.height / 3);
		fill.width = treeBounds.width / 4;
		fill.height = itemBounds.height / 3;

		return fill;
	}

	/**
	 * Set the background, foreground colors and the line style for the given diff.
	 * 
	 * @param g
	 *            the SWT GC tool.
	 * @param diff
	 *            the given Diff.
	 * @param selected
	 *            is the Diff selected or not.
	 */
	private void setGCStyleForDiff(GC g, Diff diff, boolean selected) {
		final Comparison comparison = diff.getMatch().getComparison();
		final boolean isThreeWay = comparison.isThreeWay();

		if (diff.getKind() == DifferenceKind.MOVE) {
			g.setLineStyle(SWT.LINE_DOT);
		}

		g.setForeground(fColorProvider.getCompareColor().getStrokeColor(diff, isThreeWay, false, selected));
		g.setBackground(fColorProvider.getCompareColor().getFillColor(diff, isThreeWay, false, selected));
	}

	/**
	 * Computes the bounds (as Rectangle) of the given Item.
	 * 
	 * @param event
	 *            the paint item event.
	 * @param itemWrapper
	 *            a TableItemWrapper or TreeItemWrapper.
	 * @return the bounds (as Rectangle) of the given Item.
	 */
	private static Rectangle getBounds(Event event, AbstractTableOrTreeItemWrapper itemWrapper) {
		Scrollable tree = itemWrapper.getParent();
		Rectangle treeBounds = tree.getClientArea();
		Rectangle itemBounds = itemWrapper.getBounds();
		Rectangle imageBounds = itemWrapper.getImageBounds(0);

		Rectangle fill = new Rectangle(0, 0, 0, 0);
		fill.x = itemBounds.x - imageBounds.width;
		fill.y = itemBounds.y;
		if (!"cocoa".equals(SWT.getPlatform())) { //$NON-NLS-1$
			fill.y += 1;
		}
		// +x to add the icon and the expand "+" if we are in a tree
		fill.width = itemBounds.width + imageBounds.width + DELTA_IMAGE_GAP;
		fill.height = itemBounds.height - 1;
		if (!"cocoa".equals(SWT.getPlatform())) { //$NON-NLS-1$
			fill.height -= 3;
		}

		final GC g = event.gc;
		// If you wish to paint the selection beyond the end of last column, you must change the clipping
		// region.
		int columnCount = itemWrapper.getParentColumnCount();
		if (event.index == columnCount - 1 || columnCount == 0) {
			int width = treeBounds.x + treeBounds.width - event.x;
			if (width > 0) {
				Region region = new Region();
				g.getClipping(region);
				region.add(event.x, event.y, width, event.height);
				g.setClipping(region);
				region.dispose();
			}
		}
		g.setAdvanced(true);

		return fill;
	}

	/**
	 * Check the event indicates a user-interface component state is selected.
	 * 
	 * @param event
	 *            the event.
	 * @return true, if the event indicates a user-interface component state is selected, false otherwise.
	 */
	private static boolean isSelected(Event event) {
		return (event.detail & SWT.SELECTED) != 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractStructuredMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		getStructuredViewer().getControl().removeListener(SWT.MeasureItem, fMesureItemListener);
		getStructuredViewer().getControl().removeListener(SWT.EraseItem, fEraseItemListener);
		getStructuredViewer().getControl().removeListener(SWT.PaintItem, fPaintItemListener);
		super.handleDispose(event);
	}

	/**
	 * A specific implementation of {@link IElementComparer} that compare EMF Compare Viewer Items.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 * @since 4.0
	 */
	public static final class ElementComparer implements IElementComparer {
		/**
		 * {@inheritDoc}.
		 */
		public int hashCode(Object element) {
			final int hashCode;
			if (element instanceof IMergeViewerItem) {
				IMergeViewerItem item = (IMergeViewerItem)element;
				Diff diff = item.getDiff();
				if (diff != null && diff.getConflict() != null
						&& diff.getConflict().getKind() == ConflictKind.PSEUDO) {
					// we do not create only one item per diff in pseudo conflict, so we hash the conflict and
					// not the diff
					hashCode = Objects.hashCode(item.getAncestor(), diff.getConflict());
				} else if (diff != null && item.getLeft() == null && item.getRight() == null) {
					hashCode = Objects.hashCode(item.getAncestor(), diff);
				} else {
					hashCode = Objects.hashCode(item.getLeft(), item.getRight(), item.getAncestor(), diff);
				}
			} else {
				hashCode = element.hashCode();
			}
			return hashCode;
		}

		/**
		 * {@inheritDoc}.
		 */
		public boolean equals(Object a, Object b) {
			final boolean ret;
			if (a != b && a instanceof IMergeViewerItem && b instanceof IMergeViewerItem) {
				IMergeViewerItem itemA = (IMergeViewerItem)a;
				IMergeViewerItem itemB = (IMergeViewerItem)b;
				Diff diffA = itemA.getDiff();
				Diff diffB = itemB.getDiff();
				if (diffA != null && diffA.getConflict() != null
						&& diffA.getConflict().getKind() == ConflictKind.PSEUDO && diffB != null
						&& diffB.getConflict() != null
						&& diffB.getConflict().getKind() == ConflictKind.PSEUDO) {
					// pseudo conflict
					ret = Objects.equal(itemA.getAncestor(), itemB.getAncestor())
							&& Objects.equal(diffA.getConflict(), diffB.getConflict());
				} else if (diffA != null && diffB != null && itemA.getLeft() == null
						&& itemA.getRight() == null && itemB.getLeft() == null && itemB.getRight() == null) {
					ret = Objects.equal(diffA, diffB);
				} else {
					ret = Objects.equal(itemA.getLeft(), itemB.getLeft())
							&& Objects.equal(itemA.getRight(), itemB.getRight())
							&& Objects.equal(itemA.getAncestor(), itemB.getAncestor())
							&& Objects.equal(diffA, diffB);
				}
			} else {
				ret = Objects.equal(a, b);
			}
			return ret;
		}
	}

	/**
	 * This will be used in order to resize the table items to an even height. Otherwise the lines we draw
	 * around it look somewhat flattened.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 * @since 4.0
	 */
	public static class MesureItemListener implements Listener {
		/**
		 * The height to which we will resize items.
		 */
		private int fHeight = Integer.MIN_VALUE;

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event event) {
			if (fHeight == Integer.MIN_VALUE) {
				AbstractTableOrTreeItemWrapper itemWrapper = AbstractTableOrTreeItemWrapper
						.create((Item)event.item);
				Rectangle imageBounds = itemWrapper.getImageBounds(0);
				fHeight = imageBounds.height + 3;
			}
			event.height = fHeight;
		}
	}
}
