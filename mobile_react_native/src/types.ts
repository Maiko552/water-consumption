export type LoginPayload = {
  email: string;
  password: string;
};

export type LoginResponse = {
  token: string;
};

export type WaterExpense = {
  id: number | null;
  referenceDate: string;
  dueDate: string;
  totalAmount: number;
  consumptionM3: number | null;
  waterAmount: number | null;
  sewageAmount: number | null;
  meterReading: number | null;
  isPaid: boolean;
  note: string | null;
  createdDate: string;
};

export type CreateWaterExpensePayload = {
  referenceDate: string;
  dueDate: string;
  totalAmount: number;
  consumptionM3: number | null;
  waterAmount: number | null;
  sewageAmount: number | null;
  meterReading: number | null;
  isPaid: boolean;
  note: string | null;
};
