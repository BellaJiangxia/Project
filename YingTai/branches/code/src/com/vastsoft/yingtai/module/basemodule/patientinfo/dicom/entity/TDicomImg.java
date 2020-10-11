package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity;

import java.util.Arrays;
import java.util.List;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.SplitStringBuilder;
import com.vastsoft.util.common.SplitStringParser;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity.ShareDicomImg;

public class TDicomImg extends ShareDicomImg {
	// private int body_part_amount;//部位数量
	private long[] body_part_ids_arr;
	private int piece_amount;// 影像数量

	public int getBody_part_amount() {
		return this.body_part_ids_arr == null ? 0 : this.body_part_ids_arr.length;
	}

	public long[] getBody_part_ids_arr() {
		return body_part_ids_arr;
	}

	public void setBody_part_ids_arr(long[] body_part_ids_arr) {
		this.body_part_ids_arr = body_part_ids_arr;
	}

	public String getBody_part_ids() {
		return new SplitStringBuilder<Long>().addAll(this.body_part_ids_arr).toString();
	}

	public void setBody_part_ids(String body_part_ids) {
		this.body_part_ids_arr = ArrayTools.transfer(SplitStringParser.parseAsLongArray(body_part_ids));
	}

	public int getPiece_amount() {
		return piece_amount;
	}

	public void setPiece_amount(int piece_amount) {
		this.piece_amount = piece_amount;
	}

	public void organize(List<? extends TSeries> listSeries) {
		if (!ArrayTools.isEmpty(this.body_part_ids_arr) && piece_amount > 0)
			return;
		this.body_part_ids_arr = new long[0];
		this.piece_amount = 0;
		if (CollectionTools.isEmpty(listSeries))
			return;
		for (TSeries series : listSeries) {
			if (!ArrayTools.contains(this.body_part_ids_arr, series.getPart_type_id()))
				this.body_part_ids_arr = (long[]) ArrayTools.add(this.body_part_ids_arr, series.getPart_type_id());
			piece_amount += series.getExpose_times();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(body_part_ids_arr);
		result = prime * result + piece_amount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDicomImg other = (TDicomImg) obj;
		if (!Arrays.equals(body_part_ids_arr, other.body_part_ids_arr))
			return false;
		if (piece_amount != other.piece_amount)
			return false;
		return true;
	}
	
	
}
