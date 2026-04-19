import React, { useMemo, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  FlatList,
  KeyboardTypeOptions,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Switch,
  Text,
  TextInput,
  View,
  StyleProp,
  ViewStyle,
} from "react-native";
import Constants from "expo-constants";
import { StatusBar as ExpoStatusBar } from "expo-status-bar";
import {
  CreateWaterExpensePayload,
  LoginPayload,
  LoginResponse,
  WaterExpense,
} from "./src/types";

type Screen = "login" | "list" | "create";

const colors = {
  bgTop: "#edf4ff",
  bgBottom: "#f8fbff",
  card: "#ffffff",
  primary: "#2563eb",
  primaryDark: "#1e3a8a",
  primarySoft: "#dbeafe",
  text: "#0f172a",
  textMuted: "#64748b",
  border: "#dbe4f0",
  inputBg: "#f8fafc",
  successBg: "#dcfce7",
  successText: "#166534",
  dangerBg: "#fee2e2",
  dangerText: "#b91c1c",
  shadow: "rgba(15, 23, 42, 0.08)",
};

const initialForm = {
  referenceDate: "",
  dueDate: "",
  totalAmount: "",
  consumptionM3: "",
  waterAmount: "",
  sewageAmount: "",
  meterReading: "",
  note: "",
  isPaid: false,
};

const extra = Constants.expoConfig?.extra as { apiUrl?: string } | undefined;
const API_URL =
  process.env.EXPO_PUBLIC_API_URL?.replace(/\/$/, "") ||
  extra?.apiUrl?.replace(/\/$/, "") ||
  "http://192.168.1.18:8080";

export default function App() {
  const [screen, setScreen] = useState<Screen>("login");
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);
  const [messageType, setMessageType] = useState<"success" | "error">("error");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [expenses, setExpenses] = useState<WaterExpense[]>([]);
  const [form, setForm] = useState(initialForm);

  const paidCount = useMemo(
    () => expenses.filter((expense) => expense.isPaid).length,
    [expenses]
  );
  const pendingCount = expenses.length - paidCount;

  function showError(text: string) {
    setMessageType("error");
    setMessage(text);
  }

  function showSuccess(text: string) {
    setMessageType("success");
    setMessage(text);
  }

  async function request<T>(
    path: string,
    options?: RequestInit,
    authToken?: string | null
  ): Promise<T> {
    const headers = new Headers(options?.headers);
    headers.set("Content-Type", "application/json");
    if (authToken) {
      headers.set("Authorization", `Bearer ${authToken}`);
    }

    const response = await fetch(`${API_URL}${path}`, {
      ...options,
      headers,
    });

    if (!response.ok) {
      const bodyText = await response.text();
      throw new Error(bodyText || `Erro ${response.status}`);
    }

    if (response.status === 204) {
      return undefined as T;
    }

    return (await response.json()) as T;
  }

  async function handleLogin() {
    if (!email.trim() || !password.trim()) {
      showError("Preencha email e senha.");
      return;
    }

    setIsLoading(true);
    setMessage(null);

    try {
      const payload: LoginPayload = { email: email.trim(), password };
      const response = await request<LoginResponse>("/auth/login", {
        method: "POST",
        body: JSON.stringify(payload),
      });

      setPassword("");
      setToken(response.token);
      setScreen("list");
      showSuccess("Login realizado com sucesso.");
      await loadExpenses(response.token, true);
    } catch (error) {
      showError(formatError(error, "Nao foi possivel fazer login."));
    } finally {
      setIsLoading(false);
    }
  }

  async function loadExpenses(currentToken = token, preserveMessage = false) {
    if (!currentToken) {
      return;
    }

    setIsLoading(true);
    if (!preserveMessage) {
      setMessage(null);
    }

    try {
      const response = await request<WaterExpense[]>(
        "/api/water-expenses",
        { method: "GET" },
        currentToken
      );
      setExpenses(response);
      setScreen("list");
    } catch (error) {
      showError(formatError(error, "Nao foi possivel carregar as contas."));
    } finally {
      setIsLoading(false);
    }
  }

  async function handleCreateExpense() {
    if (!token) {
      showError("Voce precisa fazer login novamente.");
      setScreen("login");
      return;
    }

    if (!form.referenceDate || !form.dueDate || !form.totalAmount) {
      showError("Preencha referencia, vencimento e valor total.");
      return;
    }

    setIsLoading(true);
    setMessage(null);

    try {
      const payload: CreateWaterExpensePayload = {
        referenceDate: form.referenceDate,
        dueDate: form.dueDate,
        totalAmount: Number(form.totalAmount),
        consumptionM3: numberOrNull(form.consumptionM3),
        waterAmount: numberOrNull(form.waterAmount),
        sewageAmount: numberOrNull(form.sewageAmount),
        meterReading: numberOrNull(form.meterReading),
        isPaid: form.isPaid,
        note: form.note.trim() || null,
      };

      if (Number.isNaN(payload.totalAmount)) {
        showError("Informe um valor total valido.");
        setIsLoading(false);
        return;
      }

      await request<WaterExpense>(
        "/api/water-expenses",
        {
          method: "POST",
          body: JSON.stringify(payload),
        },
        token
      );

      setForm(initialForm);
      setScreen("list");
      showSuccess("Conta cadastrada com sucesso.");
      await loadExpenses(token, true);
    } catch (error) {
      showError(formatError(error, "Nao foi possivel salvar a conta."));
    } finally {
      setIsLoading(false);
    }
  }

  function handleLogout() {
    setToken(null);
    setExpenses([]);
    setEmail("");
    setPassword("");
    setForm(initialForm);
    setScreen("login");
    setMessage(null);
  }

  function updateForm(field: keyof typeof initialForm, value: string | boolean) {
    setForm((current) => ({
      ...current,
      [field]: value,
    }));
  }

  function showApiInfo() {
    Alert.alert(
      "API do projeto",
      `Este app aponta para:\n${API_URL}\n\nNo iPhone, deixe a API rodando no PC e troque o IP para o da sua maquina na rede local.`
    );
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      <ExpoStatusBar style="dark" />
      <StatusBar barStyle="dark-content" />
      <KeyboardAvoidingView
        behavior={Platform.OS === "ios" ? "padding" : undefined}
        style={styles.flex}
      >
        <View style={styles.background}>
          {screen === "login" ? (
            <ScrollView contentContainerStyle={styles.centeredScroll}>
              <AuthCard
                email={email}
                password={password}
                isLoading={isLoading}
                message={message}
                messageType={messageType}
                onEmailChange={setEmail}
                onPasswordChange={setPassword}
                onSubmit={handleLogin}
                onApiInfo={showApiInfo}
              />
            </ScrollView>
          ) : screen === "list" ? (
            <ScrollView contentContainerStyle={styles.screenContent}>
              <HeaderCard
                title="Historico de contas de agua"
                subtitle="Controle suas contas com uma interface pronta para Android e iPhone."
              />

              <View style={styles.row}>
                <PrimaryButton
                  label="Nova conta"
                  onPress={() => {
                    setMessage(null);
                    setScreen("create");
                  }}
                  style={styles.flexButton}
                />
                <SecondaryButton
                  label="Atualizar"
                  onPress={() => {
                    void loadExpenses();
                  }}
                  style={styles.flexButton}
                />
              </View>

              <DangerButton label="Sair da conta" onPress={handleLogout} />

              <StatCard
                label="Total de contas"
                value={String(expenses.length)}
                hint="Quantidade cadastrada"
              />

              <View style={styles.row}>
                <StatCard
                  label="Pagas"
                  value={String(paidCount)}
                  hint="Lancamentos quitados"
                  compact
                />
                <StatCard
                  label="Pendentes"
                  value={String(pendingCount)}
                  hint="Contas em aberto"
                  compact
                />
              </View>

              {message ? (
                <MessageBanner text={message} type={messageType} />
              ) : null}

              {isLoading ? (
                <LoadingCard />
              ) : expenses.length === 0 ? (
                <EmptyCard onCreate={() => setScreen("create")} />
              ) : (
                <FlatList
                  data={expenses}
                  scrollEnabled={false}
                  keyExtractor={(item, index) => `${item.id ?? index}-${item.referenceDate}`}
                  renderItem={({ item }) => <ExpenseCard item={item} />}
                  ItemSeparatorComponent={() => <View style={styles.separator} />}
                />
              )}
            </ScrollView>
          ) : (
            <ScrollView contentContainerStyle={styles.screenContent}>
              <HeaderCard
                title="Cadastrar conta de agua"
                subtitle="Registre consumo, vencimento, pagamento e observacoes."
              />

              {message ? (
                <MessageBanner text={message} type={messageType} />
              ) : null}

              <View style={styles.formCard}>
                <FormField
                  label="Referencia (yyyy-MM-dd)"
                  value={form.referenceDate}
                  onChangeText={(value) => updateForm("referenceDate", value)}
                />
                <FormField
                  label="Vencimento (yyyy-MM-dd)"
                  value={form.dueDate}
                  onChangeText={(value) => updateForm("dueDate", value)}
                />
                <FormField
                  label="Valor total"
                  value={form.totalAmount}
                  onChangeText={(value) => updateForm("totalAmount", value)}
                  keyboardType="decimal-pad"
                />
                <FormField
                  label="Consumo m3"
                  value={form.consumptionM3}
                  onChangeText={(value) => updateForm("consumptionM3", value)}
                  keyboardType="decimal-pad"
                />
                <FormField
                  label="Valor agua"
                  value={form.waterAmount}
                  onChangeText={(value) => updateForm("waterAmount", value)}
                  keyboardType="decimal-pad"
                />
                <FormField
                  label="Valor esgoto"
                  value={form.sewageAmount}
                  onChangeText={(value) => updateForm("sewageAmount", value)}
                  keyboardType="decimal-pad"
                />
                <FormField
                  label="Leitura do hidrometro"
                  value={form.meterReading}
                  onChangeText={(value) => updateForm("meterReading", value)}
                  keyboardType="decimal-pad"
                />
                <FormField
                  label="Observacao"
                  value={form.note}
                  onChangeText={(value) => updateForm("note", value)}
                  multiline
                />

                <View style={styles.switchRow}>
                  <Text style={styles.switchLabel}>Conta paga</Text>
                  <Switch
                    value={form.isPaid}
                    onValueChange={(value) => updateForm("isPaid", value)}
                    trackColor={{ false: "#cbd5e1", true: "#93c5fd" }}
                    thumbColor={form.isPaid ? colors.primary : "#ffffff"}
                  />
                </View>
              </View>

              {isLoading ? <LoadingCard /> : null}

              <View style={styles.row}>
                <SecondaryButton
                  label="Voltar"
                  onPress={() => {
                    setMessage(null);
                    setScreen("list");
                  }}
                  style={styles.flexButton}
                />
                <PrimaryButton
                  label="Salvar conta"
                  onPress={() => {
                    void handleCreateExpense();
                  }}
                  style={styles.flexButton}
                />
              </View>
            </ScrollView>
          )}
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

function AuthCard(props: {
  email: string;
  password: string;
  isLoading: boolean;
  message: string | null;
  messageType: "success" | "error";
  onEmailChange: (value: string) => void;
  onPasswordChange: (value: string) => void;
  onSubmit: () => void;
  onApiInfo: () => void;
}) {
  return (
    <View style={styles.authCard}>
      <Badge />
      <Text style={styles.authTitle}>Consumo de Agua</Text>
      <Text style={styles.authSubtitle}>
        Entre com a mesma API do backend para apresentar no Android ou iPhone.
      </Text>

      <FormField
        label="Email"
        value={props.email}
        onChangeText={props.onEmailChange}
        keyboardType="email-address"
      />
      <FormField
        label="Senha"
        value={props.password}
        onChangeText={props.onPasswordChange}
        secureTextEntry
      />

      <PrimaryButton
        label={props.isLoading ? "Entrando..." : "Entrar"}
        onPress={props.onSubmit}
      />

      <SecondaryButton label="Ver API configurada" onPress={props.onApiInfo} />

      {props.isLoading ? <LoadingCard compact /> : null}

      {props.message ? (
        <MessageBanner text={props.message} type={props.messageType} />
      ) : null}

      <Text style={styles.footerText}>Projeto academico | Demo cross-platform</Text>
    </View>
  );
}

function HeaderCard(props: { title: string; subtitle: string }) {
  return (
    <View style={styles.card}>
      <View style={styles.headerRow}>
        <Badge />
        <View style={styles.headerTextBox}>
          <Text style={styles.headerTitle}>{props.title}</Text>
          <Text style={styles.headerSubtitle}>{props.subtitle}</Text>
        </View>
      </View>
    </View>
  );
}

function ExpenseCard({ item }: { item: WaterExpense }) {
  return (
    <View style={styles.card}>
      <View style={styles.expenseTopRow}>
        <View style={styles.headerTextBox}>
          <Text style={styles.expenseTitle}>{item.referenceDate}</Text>
          <Text style={styles.mutedText}>Vencimento: {item.dueDate}</Text>
        </View>
        <StatusChip paid={item.isPaid} />
      </View>

      <Text style={styles.expenseValue}>Valor total: R$ {formatMoney(item.totalAmount)}</Text>
      <Text style={styles.mutedText}>Consumo: {item.consumptionM3 ?? "-"} m3</Text>
      <Text style={styles.mutedText}>Observacao: {item.note || "-"}</Text>
    </View>
  );
}

function EmptyCard(props: { onCreate: () => void }) {
  return (
    <View style={styles.card}>
      <Badge centered />
      <Text style={styles.emptyTitle}>Nenhuma conta cadastrada</Text>
      <Text style={styles.emptyDescription}>
        Cadastre sua primeira conta para demonstrar o projeto no celular.
      </Text>
      <PrimaryButton label="Cadastrar primeira conta" onPress={props.onCreate} />
    </View>
  );
}

function LoadingCard(props: { compact?: boolean }) {
  return (
    <View style={[styles.card, props.compact ? styles.compactLoadingCard : null]}>
      <ActivityIndicator size="small" color={colors.primary} />
      <Text style={styles.mutedText}>Carregando...</Text>
    </View>
  );
}

function StatCard(props: {
  label: string;
  value: string;
  hint: string;
  compact?: boolean;
}) {
  return (
    <View style={[styles.card, props.compact ? styles.flexButton : null]}>
      <Text style={styles.statLabel}>{props.label}</Text>
      <Text style={styles.statValue}>{props.value}</Text>
      <Text style={styles.mutedText}>{props.hint}</Text>
    </View>
  );
}

function MessageBanner(props: { text: string; type: "success" | "error" }) {
  const isSuccess = props.type === "success";
  return (
    <View
      style={[
        styles.messageBanner,
        isSuccess ? styles.successBanner : styles.errorBanner,
      ]}
    >
      <Text style={[styles.messageText, isSuccess ? styles.successText : styles.errorText]}>
        {props.text}
      </Text>
    </View>
  );
}

function FormField(props: {
  label: string;
  value: string;
  onChangeText: (value: string) => void;
  secureTextEntry?: boolean;
  keyboardType?: KeyboardTypeOptions;
  multiline?: boolean;
}) {
  return (
    <View style={styles.fieldWrapper}>
      <Text style={styles.fieldLabel}>{props.label}</Text>
      <TextInput
        value={props.value}
        onChangeText={props.onChangeText}
        secureTextEntry={props.secureTextEntry}
        keyboardType={props.keyboardType}
        multiline={props.multiline}
        style={[styles.input, props.multiline ? styles.multilineInput : null]}
        placeholderTextColor="#94a3b8"
      />
    </View>
  );
}

function PrimaryButton(props: {
  label: string;
  onPress: () => void;
  style?: StyleProp<ViewStyle>;
}) {
  return (
    <Pressable onPress={props.onPress} style={[styles.button, styles.primaryButton, props.style]}>
      <Text style={styles.primaryButtonText}>{props.label}</Text>
    </Pressable>
  );
}

function SecondaryButton(props: {
  label: string;
  onPress: () => void;
  style?: StyleProp<ViewStyle>;
}) {
  return (
    <Pressable
      onPress={props.onPress}
      style={[styles.button, styles.secondaryButton, props.style]}
    >
      <Text style={styles.secondaryButtonText}>{props.label}</Text>
    </Pressable>
  );
}

function DangerButton(props: {
  label: string;
  onPress: () => void;
  style?: StyleProp<ViewStyle>;
}) {
  return (
    <Pressable onPress={props.onPress} style={[styles.button, styles.dangerButton, props.style]}>
      <Text style={styles.primaryButtonText}>{props.label}</Text>
    </Pressable>
  );
}

function Badge(props: { centered?: boolean } = {}) {
  return (
    <View style={[styles.badge, props.centered ? styles.badgeCentered : null]}>
      <Text style={styles.badgeText}>AG</Text>
    </View>
  );
}

function StatusChip({ paid }: { paid: boolean }) {
  return (
    <View style={[styles.chip, paid ? styles.successBanner : styles.errorBanner]}>
      <Text style={[styles.chipText, paid ? styles.successText : styles.errorText]}>
        {paid ? "Pago" : "Pendente"}
      </Text>
    </View>
  );
}

function formatMoney(value: number) {
  return value.toFixed(2).replace(".", ",");
}

function numberOrNull(value: string) {
  if (!value.trim()) {
    return null;
  }
  const parsed = Number(value.replace(",", "."));
  return Number.isNaN(parsed) ? null : parsed;
}

function formatError(error: unknown, fallback: string) {
  if (error instanceof Error && error.message.trim()) {
    return error.message;
  }
  return fallback;
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: colors.bgTop,
  },
  flex: {
    flex: 1,
  },
  background: {
    flex: 1,
    backgroundColor: colors.bgTop,
  },
  centeredScroll: {
    flexGrow: 1,
    justifyContent: "center",
    padding: 20,
  },
  screenContent: {
    padding: 16,
    gap: 12,
  },
  authCard: {
    backgroundColor: colors.card,
    borderRadius: 28,
    padding: 20,
    gap: 14,
    shadowColor: colors.shadow,
    shadowOpacity: 1,
    shadowRadius: 18,
    shadowOffset: { width: 0, height: 10 },
    elevation: 4,
  },
  card: {
    backgroundColor: colors.card,
    borderRadius: 24,
    padding: 18,
    gap: 8,
    shadowColor: colors.shadow,
    shadowOpacity: 1,
    shadowRadius: 18,
    shadowOffset: { width: 0, height: 10 },
    elevation: 3,
  },
  formCard: {
    backgroundColor: colors.card,
    borderRadius: 24,
    padding: 18,
    gap: 12,
    shadowColor: colors.shadow,
    shadowOpacity: 1,
    shadowRadius: 18,
    shadowOffset: { width: 0, height: 10 },
    elevation: 3,
  },
  compactLoadingCard: {
    alignItems: "center",
  },
  badge: {
    alignSelf: "flex-start",
    backgroundColor: colors.primary,
    borderRadius: 18,
    paddingHorizontal: 18,
    paddingVertical: 12,
  },
  badgeCentered: {
    alignSelf: "center",
  },
  badgeText: {
    color: "#ffffff",
    fontWeight: "800",
    fontSize: 18,
  },
  authTitle: {
    fontSize: 22,
    lineHeight: 28,
    fontWeight: "800",
    color: colors.text,
    textAlign: "center",
  },
  authSubtitle: {
    fontSize: 15,
    lineHeight: 22,
    textAlign: "center",
    color: colors.textMuted,
  },
  fieldWrapper: {
    gap: 6,
  },
  fieldLabel: {
    fontSize: 13,
    color: colors.primary,
    fontWeight: "600",
  },
  input: {
    backgroundColor: colors.inputBg,
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: 16,
    paddingHorizontal: 14,
    paddingVertical: 14,
    fontSize: 16,
    color: colors.text,
  },
  multilineInput: {
    minHeight: 110,
    textAlignVertical: "top",
  },
  button: {
    borderRadius: 16,
    paddingVertical: 16,
    paddingHorizontal: 18,
    alignItems: "center",
    justifyContent: "center",
  },
  primaryButton: {
    backgroundColor: colors.primary,
  },
  secondaryButton: {
    backgroundColor: "#eef2ff",
  },
  dangerButton: {
    backgroundColor: "#ef4444",
  },
  primaryButtonText: {
    color: "#ffffff",
    fontSize: 15,
    fontWeight: "800",
  },
  secondaryButtonText: {
    color: colors.primaryDark,
    fontSize: 15,
    fontWeight: "700",
  },
  footerText: {
    fontSize: 12,
    textAlign: "center",
    color: colors.textMuted,
    marginTop: 4,
  },
  headerRow: {
    flexDirection: "row",
    gap: 14,
    alignItems: "center",
  },
  headerTextBox: {
    flex: 1,
    gap: 4,
  },
  headerTitle: {
    fontSize: 22,
    lineHeight: 28,
    fontWeight: "800",
    color: colors.primaryDark,
  },
  headerSubtitle: {
    fontSize: 14,
    lineHeight: 20,
    color: colors.textMuted,
  },
  row: {
    flexDirection: "row",
    gap: 12,
  },
  flexButton: {
    flex: 1,
  },
  statLabel: {
    fontSize: 12,
    fontWeight: "600",
    color: colors.textMuted,
  },
  statValue: {
    fontSize: 28,
    lineHeight: 34,
    fontWeight: "800",
    color: colors.text,
  },
  messageBanner: {
    borderRadius: 18,
    paddingHorizontal: 16,
    paddingVertical: 14,
  },
  successBanner: {
    backgroundColor: colors.successBg,
  },
  errorBanner: {
    backgroundColor: colors.dangerBg,
  },
  messageText: {
    fontSize: 14,
    lineHeight: 20,
    fontWeight: "600",
  },
  successText: {
    color: colors.successText,
  },
  errorText: {
    color: colors.dangerText,
  },
  emptyTitle: {
    fontSize: 20,
    lineHeight: 26,
    fontWeight: "800",
    color: colors.primaryDark,
    textAlign: "center",
    marginTop: 8,
  },
  emptyDescription: {
    fontSize: 14,
    lineHeight: 20,
    color: colors.textMuted,
    textAlign: "center",
    marginBottom: 6,
  },
  expenseTopRow: {
    flexDirection: "row",
    alignItems: "flex-start",
    justifyContent: "space-between",
    gap: 12,
  },
  expenseTitle: {
    fontSize: 18,
    lineHeight: 24,
    fontWeight: "800",
    color: colors.primaryDark,
  },
  expenseValue: {
    fontSize: 16,
    lineHeight: 22,
    fontWeight: "700",
    color: colors.text,
  },
  mutedText: {
    fontSize: 14,
    lineHeight: 20,
    color: colors.textMuted,
  },
  chip: {
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 6,
  },
  chipText: {
    fontSize: 12,
    fontWeight: "800",
  },
  separator: {
    height: 12,
  },
  switchRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    gap: 12,
    marginTop: 4,
  },
  switchLabel: {
    fontSize: 15,
    fontWeight: "600",
    color: colors.text,
  },
});
