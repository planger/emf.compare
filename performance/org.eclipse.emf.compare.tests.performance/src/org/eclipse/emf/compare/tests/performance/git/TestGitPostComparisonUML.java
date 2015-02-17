/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance.git;

import org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest;
import org.eclipse.emf.compare.tests.performance.TestPostComparisonUML;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import data.models.DataGit;
import data.models.NominalGitInputData;
import data.models.NominalSplitGitInputData;
import data.models.SmallGitInputData;
import data.models.SmallSplitGitInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGitPostComparisonUML extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestPostComparisonUML.class.getSimpleName());
	}

	@Test
	public void a_pcUMLUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pcUMLUMLSmall");
		
		final DataGit data = new SmallGitInputData();
		data.match();
		data.postMatchUML();
		data.diff();
		data.req();
		data.equi();
		data.conflict();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postComparisonUML();
			}
		});
		data.dispose();
	}
	
	@Test
	public void b_pcUMLUMLNominal() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pcUMLUMLNominal");
		
		final DataGit data = new NominalGitInputData();
		data.match();
		data.postMatchUML();
		data.diff();
		data.req();
		data.equi();
		data.conflict();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postComparisonUML();
			}
		});
		data.dispose();
	}
	
	@Test
	public void c_pcUMLUMLSmallSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pcUMLUMLSmallSplit");
		
		final DataGit data = new SmallSplitGitInputData();
		data.match();
		data.postMatchUML();
		data.diff();
		data.req();
		data.equi();
		data.conflict();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postComparisonUML();
			}
		});
		data.dispose();
	}
	
	@Test
	public void d_pcUMLUMLNominalSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pcUMLUMLNominalSplit");
		
		final DataGit data = new NominalSplitGitInputData();
		data.match();
		data.postMatchUML();
		data.diff();
		data.req();
		data.equi();
		data.conflict();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postComparisonUML();
			}
		});
		data.dispose();
	}
}
