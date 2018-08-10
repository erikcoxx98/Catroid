/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.view.View;
import android.widget.Spinner;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.adapter.DataAdapter;
import org.catrobat.catroid.ui.adapter.UserVariableAdapterWrapper;

import java.util.List;

public class ChangeVariableBrick extends UserVariableBrick {

	private static final long serialVersionUID = 1L;

	public ChangeVariableBrick() {
		this(new Formula(BrickValues.CHANGE_VARIABLE));
	}

	public ChangeVariableBrick(double value) {
		this(new Formula(value));
	}

	public ChangeVariableBrick(Formula formula, UserVariable userVariable) {
		this(formula);
		this.userVariable = userVariable;
	}

	public ChangeVariableBrick(Formula formula) {
		addAllowedBrickField(BrickField.VARIABLE_CHANGE, R.id.brick_change_variable_edit_text);
		setFormulaWithBrickField(BrickField.VARIABLE_CHANGE, formula);
	}

	@Override
	public int getViewResource() {
		return R.layout.brick_change_variable_by;
	}

	@Override
	public View getView(Context context) {
		super.getView(context);

		Spinner variableSpinner = view.findViewById(R.id.change_variable_spinner);

		DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentlyEditedScene().getDataContainer()
				.createDataAdapter(context, ProjectManager.getInstance().getCurrentSprite());

		UserVariableAdapterWrapper userVariableAdapterWrapper = new UserVariableAdapterWrapper(context,
				dataAdapter);

		userVariableAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);
		variableSpinner.setAdapter(userVariableAdapterWrapper);
		setSpinnerSelection(variableSpinner, null);
		variableSpinner.setOnTouchListener(createSpinnerOnTouchListener());
		variableSpinner.setOnItemSelectedListener(createVariableSpinnerItemSelectedListener());
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = super.getPrototypeView(context);
		Spinner variableSpinner = prototypeView.findViewById(R.id.change_variable_spinner);

		DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentlyEditedScene()
				.getDataContainer().createDataAdapter(context, ProjectManager.getInstance().getCurrentSprite());

		UserVariableAdapterWrapper userVariableAdapterWrapper = new UserVariableAdapterWrapper(context, dataAdapter);
		userVariableAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);
		variableSpinner.setAdapter(userVariableAdapterWrapper);
		setSpinnerSelection(variableSpinner, null);

		return prototypeView;
	}

	@Override
	public void onNewVariable(UserVariable userVariable) {
		Spinner spinner = view.findViewById(R.id.change_variable_spinner);
		setSpinnerSelection(spinner, userVariable);
	}

	@Override
	public List<ScriptSequenceAction> addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory()
				.createChangeVariableAction(sprite, getFormulaWithBrickField(BrickField.VARIABLE_CHANGE), userVariable));
		return null;
	}
}
